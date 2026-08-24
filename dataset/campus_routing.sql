-- ==============================================================
-- Innovative-Minds Campus Routing System
-- campus_routing.sql
--
-- HOW TO USE
-- 1. Download DB Browser for SQLite (free):
--       https://sqlitebrowser.org/dl/
-- 2. Open DB Browser → File → New Database
--    Save it anywhere, e.g. campus_routing.db
-- 3. Click the "Execute SQL" tab
-- 4. Open this file: File → Open SQL file → campus_routing.sql
-- 5. Press F5 (or click the ► Run button)
-- 6. Click "Browse Data" to see all tables and rows
-- ==============================================================

PRAGMA foreign_keys = ON;

-- ── Table: locations ─────────────────────────────────────────
-- Nodes in the campus service network.
-- location_id  : natural key from the dataset  (e.g. "L001")
-- name         : human-readable place name
-- latitude     : optional GPS latitude
-- longitude    : optional GPS longitude
-- category     : 'academic' | 'hall' | 'service' | 'general'
-- is_active    : 1 = active in routing, 0 = soft-deleted
-- created_at   : auto-set on insert
CREATE TABLE IF NOT EXISTS locations (
    location_id  TEXT     NOT NULL,
    name         TEXT     NOT NULL,
    latitude     REAL,
    longitude    REAL,
    category     TEXT     NOT NULL DEFAULT 'general',
    is_active    INTEGER  NOT NULL DEFAULT 1 CHECK (is_active IN (0, 1)),
    created_at   TEXT     NOT NULL
                          DEFAULT (strftime('%Y-%m-%dT%H:%M:%fZ','now')),

    CONSTRAINT pk_locations      PRIMARY KEY (location_id),
    CONSTRAINT uq_locations_name UNIQUE      (name)
);

-- ── Table: resources ─────────────────────────────────────────
-- Vehicles, officers, staff, riders or equipment that can be
-- assigned to a service request.
-- type   : 'vehicle' | 'officer' | 'staff' | 'rider' | 'equipment'
-- status : 'available' | 'busy' | 'offline'
CREATE TABLE IF NOT EXISTS resources (
    resource_id          INTEGER  NOT NULL,
    name                 TEXT     NOT NULL,
    type                 TEXT     NOT NULL
                                  CHECK (type IN
                                      ('vehicle','officer','staff',
                                       'rider','equipment')),
    current_location_id  TEXT,
    status               TEXT     NOT NULL DEFAULT 'available'
                                  CHECK (status IN
                                      ('available','busy','offline')),
    capacity             REAL,
    registered_at        TEXT     NOT NULL
                                  DEFAULT (strftime('%Y-%m-%dT%H:%M:%fZ','now')),

    CONSTRAINT pk_resources    PRIMARY KEY (resource_id AUTOINCREMENT),
    CONSTRAINT fk_res_location FOREIGN KEY (current_location_id)
                               REFERENCES locations (location_id)
                               ON UPDATE CASCADE ON DELETE SET NULL
);

-- ── Table: roads ─────────────────────────────────────────────
-- Weighted directed edges between locations (mirrors Extent.java).
-- distance_metres  : Dijkstra edge weight (integer metres)
-- travel_time_mins : supplementary time weight
-- is_bidirectional : 1 = reverse edge implied, 0 = one-way
-- is_active        : 0 = road temporarily closed
CREATE TABLE IF NOT EXISTS roads (
    road_id          INTEGER  NOT NULL,
    source_id        TEXT     NOT NULL,
    destination_id   TEXT     NOT NULL,
    distance_metres  INTEGER  NOT NULL CHECK (distance_metres >= 0),
    travel_time_mins REAL              CHECK (travel_time_mins >= 0),
    road_name        TEXT,
    is_bidirectional INTEGER  NOT NULL DEFAULT 1
                             CHECK (is_bidirectional IN (0, 1)),
    is_active        INTEGER  NOT NULL DEFAULT 1
                             CHECK (is_active IN (0, 1)),
    created_at       TEXT     NOT NULL
                             DEFAULT (strftime('%Y-%m-%dT%H:%M:%fZ','now')),

    CONSTRAINT pk_roads             PRIMARY KEY (road_id AUTOINCREMENT),
    CONSTRAINT uq_roads_directed    UNIQUE (source_id, destination_id),
    CONSTRAINT fk_roads_source      FOREIGN KEY (source_id)
                                    REFERENCES locations (location_id)
                                    ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_roads_destination FOREIGN KEY (destination_id)
                                    REFERENCES locations (location_id)
                                    ON UPDATE CASCADE ON DELETE RESTRICT
);

-- ── Table: service_requests ──────────────────────────────────
-- Jobs queued, prioritised, searched and sorted.
-- priority : 1 (low) – 10 (urgent)
-- status   : 'pending' | 'assigned' | 'in_progress' |
--            'completed' | 'cancelled'
CREATE TABLE IF NOT EXISTS service_requests (
    request_id     INTEGER  NOT NULL,
    title          TEXT     NOT NULL,
    description    TEXT,
    origin_id      TEXT     NOT NULL,
    destination_id TEXT     NOT NULL,
    priority       INTEGER  NOT NULL DEFAULT 5
                           CHECK (priority BETWEEN 1 AND 10),
    status         TEXT     NOT NULL DEFAULT 'pending'
                           CHECK (status IN
                               ('pending','assigned','in_progress',
                                'completed','cancelled')),
    assigned_to    INTEGER,
    requested_at   TEXT     NOT NULL
                           DEFAULT (strftime('%Y-%m-%dT%H:%M:%fZ','now')),
    deadline_at    TEXT,
    completed_at   TEXT,

    CONSTRAINT pk_service_requests PRIMARY KEY (request_id AUTOINCREMENT),
    CONSTRAINT fk_sr_origin        FOREIGN KEY (origin_id)
                                   REFERENCES locations (location_id)
                                   ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_sr_destination   FOREIGN KEY (destination_id)
                                   REFERENCES locations (location_id)
                                   ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_sr_assigned_to   FOREIGN KEY (assigned_to)
                                   REFERENCES resources (resource_id)
                                   ON UPDATE CASCADE ON DELETE SET NULL
);

-- ── Table: algorithm_runs ────────────────────────────────────
-- Empirical Dijkstra runtime measurements for complexity analysis.
-- vertex_count : |V| at runtime
-- edge_count   : |E| at runtime
-- duration_ms  : wall-clock execution time in milliseconds
CREATE TABLE IF NOT EXISTS algorithm_runs (
    run_id          INTEGER  NOT NULL,
    algorithm_name  TEXT     NOT NULL,
    vertex_count    INTEGER  NOT NULL CHECK (vertex_count >= 0),
    edge_count      INTEGER  NOT NULL CHECK (edge_count   >= 0),
    source_id       TEXT,
    destination_id  TEXT,
    path_length_m   INTEGER,
    hops            INTEGER  CHECK (hops >= 0),
    duration_ms     REAL     NOT NULL CHECK (duration_ms >= 0),
    notes           TEXT,
    recorded_at     TEXT     NOT NULL
                            DEFAULT (strftime('%Y-%m-%dT%H:%M:%fZ','now')),

    CONSTRAINT pk_algorithm_runs  PRIMARY KEY (run_id AUTOINCREMENT),
    CONSTRAINT fk_ar_source       FOREIGN KEY (source_id)
                                  REFERENCES locations (location_id)
                                  ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_ar_destination  FOREIGN KEY (destination_id)
                                  REFERENCES locations (location_id)
                                  ON UPDATE CASCADE ON DELETE SET NULL
);

-- ── Table: audit_events ──────────────────────────────────────
-- Append-only stack-based undo/audit log. Rows are never modified.
-- event_type      : 'INSERT' | 'UPDATE' | 'DELETE' | 'UNDO' |
--                   'ROUTE_CALCULATED' | 'REQUEST_ASSIGNED' | …
-- parent_event_id : links an UNDO back to the event it reverses
-- payload_before  : JSON snapshot before the change
-- payload_after   : JSON snapshot after the change
-- stack_depth     : undo-stack depth at time of event
CREATE TABLE IF NOT EXISTS audit_events (
    event_id        INTEGER  NOT NULL,
    event_type      TEXT     NOT NULL,
    table_name      TEXT,
    record_id       TEXT,
    parent_event_id INTEGER,
    actor           TEXT,
    payload_before  TEXT,
    payload_after   TEXT,
    stack_depth     INTEGER  NOT NULL DEFAULT 0 CHECK (stack_depth >= 0),
    occurred_at     TEXT     NOT NULL
                            DEFAULT (strftime('%Y-%m-%dT%H:%M:%fZ','now')),

    CONSTRAINT pk_audit_events PRIMARY KEY (event_id AUTOINCREMENT),
    CONSTRAINT fk_ae_parent    FOREIGN KEY (parent_event_id)
                               REFERENCES audit_events (event_id)
                               ON DELETE RESTRICT
);

-- ── Indexes ───────────────────────────────────────────────────
CREATE INDEX IF NOT EXISTS idx_roads_source       ON roads (source_id);
CREATE INDEX IF NOT EXISTS idx_roads_destination  ON roads (destination_id);
CREATE INDEX IF NOT EXISTS idx_sr_priority        ON service_requests (priority DESC, requested_at ASC);
CREATE INDEX IF NOT EXISTS idx_sr_status          ON service_requests (status);
CREATE INDEX IF NOT EXISTS idx_res_status         ON resources (status);
CREATE INDEX IF NOT EXISTS idx_ae_event_type      ON audit_events (event_type);
CREATE INDEX IF NOT EXISTS idx_ae_occurred_at     ON audit_events (occurred_at DESC);

-- ==============================================================
-- SEED DATA – locations  (50 rows from dataset/locations.csv)
-- ==============================================================
INSERT OR IGNORE INTO locations (location_id, name, category) VALUES
('L001', 'Main Gate',                                    'service'),
('L002', 'Legon Main Entrance',                          'service'),
('L003', 'University Square',                            'service'),
('L004', 'Registry',                                     'service'),
('L005', 'Great Hall',                                   'service'),
('L006', 'Balme Library',                                'academic'),
('L007', 'Computer Science Department',                  'academic'),
('L008', 'School of Engineering Sciences',               'academic'),
('L009', 'ICT Directorate',                              'service'),
('L010', 'Department of Mathematics',                    'academic'),
('L011', 'Department of Physics',                        'academic'),
('L012', 'Department of Chemistry',                      'academic'),
('L013', 'Department of Earth Science',                  'academic'),
('L014', 'Centre for Remote Sensing & GIS',              'academic'),
('L015', 'Institute for Environment & Sanitation Studies','academic'),
('L016', 'School of Communication Studies',              'academic'),
('L017', 'School of Performing Arts',                    'academic'),
('L018', 'University of Ghana Business School',          'academic'),
('L019', 'School of Law',                                'academic'),
('L020', 'Graduate School',                              'academic'),
('L021', 'Department of Plant & Environmental Biology',  'academic'),
('L022', 'School of Biological Sciences',                'academic'),
('L023', 'WACCBIP',                                      'academic'),
('L024', 'UG Hospital',                                  'service'),
('L025', 'School of Public Health',                      'academic'),
('L026', 'Medical School',                               'academic'),
('L027', 'Dental School',                                'academic'),
('L028', 'Legon Hall',                                   'hall'),
('L029', 'Legon Hall Annex A',                           'hall'),
('L030', 'Akuafo Hall',                                  'hall'),
('L031', 'Mensah Sarbah Hall',                           'hall'),
('L032', 'Commonwealth Hall',                            'hall'),
('L033', 'Volta Hall',                                   'hall'),
('L034', 'Jean Nelson Aka Hall',                         'hall'),
('L035', 'Diaspora Halls',                               'hall'),
('L036', 'Jubilee Hall',                                 'hall'),
('L037', 'Bush Canteen',                                 'service'),
('L038', 'Night Market',                                 'service'),
('L039', 'Banking Square',                               'service'),
('L040', 'Central Cafeteria',                            'service'),
('L041', 'Athletic Oval',                                'service'),
('L042', 'Sports Directorate',                           'service'),
('L043', 'Shuttle Terminal',                             'service'),
('L044', 'Maintenance Workshop',                         'service'),
('L045', 'Security Services HQ',                         'service'),
('L046', 'Fire Station',                                 'service'),
('L047', 'Visitor Car Park',                             'service'),
('L048', 'Staff Village Junction',                       'service'),
('L049', 'University Guest Centre',                      'service'),
('L050', 'Cedi Conference Centre',                       'service');

-- ==============================================================
-- SEED DATA – roads  (100 rows from dataset/roads.csv)
-- Distances in metres, travel time in minutes.
-- ==============================================================
INSERT OR IGNORE INTO roads (source_id, destination_id, distance_metres, travel_time_mins) VALUES
('L001','L002', 350, 4),
('L002','L003', 300, 4),
('L003','L004', 180, 2),
('L004','L005', 150, 2),
('L005','L006', 320, 4),
('L006','L007', 420, 5),
('L007','L008', 180, 2),
('L008','L009', 120, 2),
('L009','L010', 250, 3),
('L010','L011', 140, 2),
('L011','L012', 160, 2),
('L012','L013', 230, 3),
('L013','L014', 170, 2),
('L014','L015', 200, 3),
('L015','L016', 240, 3),
('L016','L017', 300, 4),
('L017','L040', 220, 3),
('L040','L037', 250, 3),
('L037','L038', 380, 5),
('L038','L039', 200, 3),
('L039','L018', 250, 3),
('L018','L050', 160, 2),
('L050','L020', 280, 4),
('L020','L019', 300, 4),
('L019','L006', 450, 6),
('L020','L021', 220, 3),
('L021','L022', 180, 2),
('L022','L023', 150, 2),
('L023','L024', 450, 6),
('L024','L025', 150, 2),
('L025','L026', 120, 2),
('L026','L027', 100, 2),
('L027','L033', 350, 5),
('L033','L032', 280, 4),
('L032','L031', 320, 4),
('L031','L028', 300, 4),
('L028','L029', 100, 2),
('L028','L030', 250, 3),
('L030','L041', 350, 5),
('L041','L042', 120, 2),
('L042','L037', 320, 4),
('L037','L049', 380, 5),
('L049','L050', 300, 4),
('L050','L039', 220, 3),
('L039','L047', 180, 2),
('L047','L001', 150, 2),
('L001','L043', 220, 3),
('L043','L048', 400, 5),
('L048','L045', 350, 4),
('L045','L046', 150, 2),
('L046','L044', 200, 3),
('L044','L024', 350, 5),
('L044','L004', 650, 8),
('L045','L004', 700, 9),
('L045','L001', 450, 6),
('L043','L003', 350, 5),
('L043','L005', 400, 5),
('L005','L018', 500, 6),
('L018','L019', 450, 6),
('L006','L018', 400, 5),
('L006','L010', 380, 5),
('L007','L011', 250, 3),
('L007','L009', 150, 2),
('L008','L012', 250, 3),
('L012','L022', 400, 5),
('L022','L025', 450, 6),
('L026','L023', 280, 4),
('L027','L032', 550, 7),
('L033','L034', 300, 4),
('L034','L035', 180, 2),
('L035','L036', 220, 3),
('L036','L038', 450, 6),
('L038','L037', 350, 5),
('L041','L040', 250, 3),
('L040','L039', 300, 4),
('L005','L016', 550, 7),
('L004','L006', 250, 3),
('L004','L018', 450, 6),
('L003','L005', 200, 3),
('L007','L019', 400, 5),
('L008','L010', 200, 3),
('L013','L022', 300, 4),
('L015','L021', 350, 5),
('L016','L049', 450, 6),
('L017','L038', 350, 5),
('L040','L049', 200, 3),
('L037','L041', 300, 4),
('L041','L030', 280, 4),
('L031','L033', 450, 6),
('L032','L034', 400, 5),
('L034','L024', 550, 7),
('L024','L045', 400, 5),
('L025','L044', 350, 5),
('L026','L042', 600, 8),
('L042','L043', 400, 5),
('L043','L047', 200, 3),
('L047','L039', 250, 3),
('L039','L001', 350, 5),
('L048','L043', 300, 4),
('L046','L001', 250, 3);

-- ==============================================================
-- SAMPLE DATA – resources (5 example rows)
-- ==============================================================
INSERT OR IGNORE INTO resources (name, type, current_location_id, status, capacity) VALUES
('Security Patrol 1',    'officer',   'L045', 'available', NULL),
('Campus Shuttle A',     'vehicle',   'L043', 'available', 20),
('Maintenance Crew 1',   'staff',     'L044', 'busy',      NULL),
('Medical Response Van', 'vehicle',   'L024', 'available', 4),
('Delivery Rider 1',     'rider',     'L001', 'available', NULL);

-- ==============================================================
-- SAMPLE DATA – service_requests (4 example rows)
-- ==============================================================
INSERT OR IGNORE INTO service_requests
    (title, description, origin_id, destination_id, priority, status, assigned_to)
VALUES
('Broken street lamp',
 'Lamp post near Balme Library not working at night.',
 'L006', 'L044', 7, 'pending', NULL),

('Medical emergency',
 'Student collapsed near Athletic Oval.',
 'L041', 'L024', 10, 'assigned', 4),

('Package delivery',
 'Documents to be taken from Registry to School of Law.',
 'L004', 'L019', 5, 'in_progress', 5),

('Security incident',
 'Suspicious activity reported at Night Market.',
 'L038', 'L045', 8, 'assigned', 1);

-- ==============================================================
-- SAMPLE DATA – algorithm_runs (3 example rows)
-- ==============================================================
INSERT OR IGNORE INTO algorithm_runs
    (algorithm_name, vertex_count, edge_count,
     source_id, destination_id, path_length_m, hops, duration_ms, notes)
VALUES
('dijkstra', 50, 100, 'L001', 'L024', 1820, 6,  2.3,  'Main Gate to UG Hospital'),
('dijkstra', 50, 100, 'L006', 'L028', 1070, 4,  1.9,  'Balme Library to Legon Hall'),
('dijkstra', 50, 100, 'L043', 'L033',  980, 3,  1.5,  'Shuttle Terminal to Volta Hall');

-- ==============================================================
-- SAMPLE DATA – audit_events (3 example rows)
-- ==============================================================
INSERT OR IGNORE INTO audit_events
    (event_type, table_name, record_id, actor, payload_after, stack_depth)
VALUES
('INSERT', 'service_requests', '1', 'system',
 '{"title":"Broken street lamp","priority":7,"status":"pending"}', 1),

('INSERT', 'service_requests', '2', 'system',
 '{"title":"Medical emergency","priority":10,"status":"assigned"}',  2),

('UPDATE', 'service_requests', '3', 'dispatcher',
 '{"status":"in_progress","assigned_to":5}', 3);

-- ==============================================================
-- Schema + seed data complete.
-- Tables : locations (50), roads (100), resources (5),
--          service_requests (4), algorithm_runs (3), audit_events (3)
-- Indexes: 7
-- ==============================================================
