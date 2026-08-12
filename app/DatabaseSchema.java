package app;

/**
 * DatabaseSchema.java
 *
 * Creates the full SQL schema for the Innovative-Minds campus routing system.
 *
 * Tables
 * ──────────────────────────────────────────────────────────────────────────
 *  locations        – Nodes in the campus service network (mirrors Locations.java)
 *  roads            – Weighted directed edges between locations (mirrors Extent.java)
 *  service_requests – Jobs queued, prioritised, searched and sorted
 *  resources        – Vehicles, officers, staff or assets that can be assigned
 *  algorithm_runs   – Empirical runtime measurements and input-size metadata
 *  audit_events     – Stack-based undo/audit trail of important system events
 * ──────────────────────────────────────────────────────────────────────────
 *
 * Run (after compiling the whole app package):
 *   java -cp build app.DatabaseSchema
 *
 * A SQLite database file "campus_routing.db" will be created in the
 * working directory.  Swap the JDBC URL for any RDBMS of your choice
 * (PostgreSQL, MySQL, H2, …) by changing DB_URL below.
 */
public class DatabaseSchema {


    // ══════════════════════════════════════════════════════════════════════
    //  DDL – CREATE TABLE statements
    // ══════════════════════════════════════════════════════════════════════

    /**
     * locations
     *
     * Stores every node (place) in the campus service network.
     * Mirrors the CSV dataset/locations.csv and the in-memory Locations class.
     *
     * Columns
     *   location_id  – Natural key from the dataset (e.g. "L001").
     *   name         – Human-readable place name.
     *   latitude     – Optional GPS latitude for map rendering.
     *   longitude    – Optional GPS longitude for map rendering.
     *   category     – Broad classification: 'academic', 'hall', 'service', etc.
     *   is_active    – Soft-delete flag; inactive nodes are hidden from routing.
     *   created_at   – ISO-8601 timestamp; set automatically on insert.
     */
    private static final String CREATE_LOCATIONS = ""
            + "CREATE TABLE IF NOT EXISTS locations (\n"
            + "    location_id  TEXT        NOT NULL,\n"
            + "    name         TEXT        NOT NULL,\n"
            + "    latitude     REAL,\n"
            + "    longitude    REAL,\n"
            + "    category     TEXT        NOT NULL DEFAULT 'general',\n"
            + "    is_active    INTEGER     NOT NULL DEFAULT 1\n"
            + "                            CHECK (is_active IN (0, 1)),\n"
            + "    created_at   TEXT        NOT NULL\n"
            + "                            DEFAULT (strftime('%Y-%m-%dT%H:%M:%fZ','now')),\n"
            + "\n"
            + "    CONSTRAINT pk_locations PRIMARY KEY (location_id),\n"
            + "    CONSTRAINT uq_locations_name UNIQUE (name)\n"
            + ");";

    /**
     * roads
     *
     * Stores weighted directed edges between locations.
     * Mirrors the in-memory Extent class and dataset/roads.csv.
     *
     * Columns
     *   road_id          – Surrogate auto-increment primary key.
     *   source_id        – FK to locations (origin node).
     *   destination_id   – FK to locations (destination node).
     *   distance_metres  – Edge weight used by Dijkstra (integer metres).
     *   travel_time_mins – Supplementary travel-time weight (minutes).
     *   road_name        – Optional street or path label.
     *   is_bidirectional – 1 = the reverse edge is implied; 0 = one-way only.
     *   is_active        – Soft-delete flag for temporary road closures.
     *   created_at       – Record creation timestamp.
     *
     * Constraint: a directed edge (source → destination) must be unique.
     */
    private static final String CREATE_ROADS = ""
            + "CREATE TABLE IF NOT EXISTS roads (\n"
            + "    road_id          INTEGER     NOT NULL,\n"
            + "    source_id        TEXT        NOT NULL,\n"
            + "    destination_id   TEXT        NOT NULL,\n"
            + "    distance_metres  INTEGER     NOT NULL CHECK (distance_metres >= 0),\n"
            + "    travel_time_mins REAL                 CHECK (travel_time_mins >= 0),\n"
            + "    road_name        TEXT,\n"
            + "    is_bidirectional INTEGER     NOT NULL DEFAULT 1\n"
            + "                                CHECK (is_bidirectional IN (0, 1)),\n"
            + "    is_active        INTEGER     NOT NULL DEFAULT 1\n"
            + "                                CHECK (is_active IN (0, 1)),\n"
            + "    created_at       TEXT        NOT NULL\n"
            + "                                DEFAULT (strftime('%Y-%m-%dT%H:%M:%fZ','now')),\n"
            + "\n"
            + "    CONSTRAINT pk_roads              PRIMARY KEY (road_id AUTOINCREMENT),\n"
            + "    CONSTRAINT uq_roads_directed     UNIQUE (source_id, destination_id),\n"
            + "    CONSTRAINT fk_roads_source       FOREIGN KEY (source_id)\n"
            + "                                     REFERENCES locations (location_id)\n"
            + "                                     ON UPDATE CASCADE ON DELETE RESTRICT,\n"
            + "    CONSTRAINT fk_roads_destination  FOREIGN KEY (destination_id)\n"
            + "                                     REFERENCES locations (location_id)\n"
            + "                                     ON UPDATE CASCADE ON DELETE RESTRICT\n"
            + ");";

    /**
     * resources
     *
     * Stores vehicles, officers, staff, riders or any asset that can be
     * assigned to a service request.
     *
     * Columns
     *   resource_id          – Surrogate auto-increment primary key.
     *   name                 – Display name (e.g. "Security Patrol 1").
     *   type                 – Resource category: 'vehicle', 'officer', 'staff',
     *                          'rider', 'equipment'.
     *   current_location_id  – FK to locations (last known position).
     *   status               – Availability: 'available', 'busy', 'offline'.
     *   capacity             – Optional numeric capacity (seats, payload kg, …).
     *   registered_at        – When the resource was added to the system.
     */
    private static final String CREATE_RESOURCES = ""
            + "CREATE TABLE IF NOT EXISTS resources (\n"
            + "    resource_id          INTEGER  NOT NULL,\n"
            + "    name                 TEXT     NOT NULL,\n"
            + "    type                 TEXT     NOT NULL\n"
            + "                                  CHECK (type IN\n"
            + "                                      ('vehicle','officer','staff',\n"
            + "                                       'rider','equipment')),\n"
            + "    current_location_id  TEXT,\n"
            + "    status               TEXT     NOT NULL DEFAULT 'available'\n"
            + "                                  CHECK (status IN\n"
            + "                                      ('available','busy','offline')),\n"
            + "    capacity             REAL,\n"
            + "    registered_at        TEXT     NOT NULL\n"
            + "                                  DEFAULT (strftime('%Y-%m-%dT%H:%M:%fZ','now')),\n"
            + "\n"
            + "    CONSTRAINT pk_resources    PRIMARY KEY (resource_id AUTOINCREMENT),\n"
            + "    CONSTRAINT fk_res_location FOREIGN KEY (current_location_id)\n"
            + "                               REFERENCES locations (location_id)\n"
            + "                               ON UPDATE CASCADE ON DELETE SET NULL\n"
            + ");";

    /**
     * service_requests
     *
     * Stores jobs that will be queued, prioritised, searched and sorted.
     * A service request represents a dispatch task originating from a location
     * and targeting another; it is routed through the campus graph.
     *
     * Columns
     *   request_id     – Surrogate auto-increment primary key.
     *   title          – Short description of the job.
     *   description    – Full free-text details.
     *   origin_id      – FK to locations (where the request originates).
     *   destination_id – FK to locations (where service must be delivered).
     *   priority       – Integer priority level 1–10; higher = more urgent.
     *   status         – Lifecycle: 'pending', 'assigned', 'in_progress',
     *                    'completed', 'cancelled'.
     *   assigned_to    – FK to resources (the assigned resource, nullable).
     *   requested_at   – When the request was lodged.
     *   deadline_at    – Optional service deadline.
     *   completed_at   – Timestamp when status moved to 'completed'.
     */
    private static final String CREATE_SERVICE_REQUESTS = ""
            + "CREATE TABLE IF NOT EXISTS service_requests (\n"
            + "    request_id     INTEGER  NOT NULL,\n"
            + "    title          TEXT     NOT NULL,\n"
            + "    description    TEXT,\n"
            + "    origin_id      TEXT     NOT NULL,\n"
            + "    destination_id TEXT     NOT NULL,\n"
            + "    priority       INTEGER  NOT NULL DEFAULT 5\n"
            + "                           CHECK (priority BETWEEN 1 AND 10),\n"
            + "    status         TEXT     NOT NULL DEFAULT 'pending'\n"
            + "                           CHECK (status IN\n"
            + "                               ('pending','assigned','in_progress',\n"
            + "                                'completed','cancelled')),\n"
            + "    assigned_to    INTEGER,\n"
            + "    requested_at   TEXT     NOT NULL\n"
            + "                           DEFAULT (strftime('%Y-%m-%dT%H:%M:%fZ','now')),\n"
            + "    deadline_at    TEXT,\n"
            + "    completed_at   TEXT,\n"
            + "\n"
            + "    CONSTRAINT pk_service_requests PRIMARY KEY (request_id AUTOINCREMENT),\n"
            + "    CONSTRAINT fk_sr_origin        FOREIGN KEY (origin_id)\n"
            + "                                   REFERENCES locations (location_id)\n"
            + "                                   ON UPDATE CASCADE ON DELETE RESTRICT,\n"
            + "    CONSTRAINT fk_sr_destination   FOREIGN KEY (destination_id)\n"
            + "                                   REFERENCES locations (location_id)\n"
            + "                                   ON UPDATE CASCADE ON DELETE RESTRICT,\n"
            + "    CONSTRAINT fk_sr_assigned_to   FOREIGN KEY (assigned_to)\n"
            + "                                   REFERENCES resources (resource_id)\n"
            + "                                   ON UPDATE CASCADE ON DELETE SET NULL\n"
            + ");";

    /**
     * algorithm_runs
     *
     * Stores empirical runtime measurements for algorithm analysis.
     * Each row captures one execution of Dijkstra (or any other algorithm),
     * recording input-size metadata and wall-clock timing so that complexity
     * curves can be plotted and compared.
     *
     * Columns
     *   run_id          – Surrogate auto-increment primary key.
     *   algorithm_name  – Identifier: 'dijkstra', 'bfs', 'dfs', 'a_star', …
     *   vertex_count    – Number of vertices |V| in the graph at runtime.
     *   edge_count      – Number of edges   |E| in the graph at runtime.
     *   source_id       – FK to locations (Dijkstra source node).
     *   destination_id  – FK to locations (Dijkstra target node, if applicable).
     *   path_length_m   – Shortest-path distance found (metres); null = no path.
     *   hops            – Number of edges traversed in the optimal path.
     *   duration_ms     – Wall-clock execution time in milliseconds.
     *   notes           – Free-text annotation (e.g. "cold run", "cached").
     *   recorded_at     – Timestamp of the measurement.
     */
    private static final String CREATE_ALGORITHM_RUNS = ""
            + "CREATE TABLE IF NOT EXISTS algorithm_runs (\n"
            + "    run_id          INTEGER  NOT NULL,\n"
            + "    algorithm_name  TEXT     NOT NULL,\n"
            + "    vertex_count    INTEGER  NOT NULL CHECK (vertex_count >= 0),\n"
            + "    edge_count      INTEGER  NOT NULL CHECK (edge_count   >= 0),\n"
            + "    source_id       TEXT,\n"
            + "    destination_id  TEXT,\n"
            + "    path_length_m   INTEGER,\n"
            + "    hops            INTEGER  CHECK (hops >= 0),\n"
            + "    duration_ms     REAL     NOT NULL CHECK (duration_ms >= 0),\n"
            + "    notes           TEXT,\n"
            + "    recorded_at     TEXT     NOT NULL\n"
            + "                            DEFAULT (strftime('%Y-%m-%dT%H:%M:%fZ','now')),\n"
            + "\n"
            + "    CONSTRAINT pk_algorithm_runs  PRIMARY KEY (run_id AUTOINCREMENT),\n"
            + "    CONSTRAINT fk_ar_source       FOREIGN KEY (source_id)\n"
            + "                                  REFERENCES locations (location_id)\n"
            + "                                  ON UPDATE CASCADE ON DELETE SET NULL,\n"
            + "    CONSTRAINT fk_ar_destination  FOREIGN KEY (destination_id)\n"
            + "                                  REFERENCES locations (location_id)\n"
            + "                                  ON UPDATE CASCADE ON DELETE SET NULL\n"
            + ");";

    /**
     * audit_events
     *
     * Append-only log of important system events supporting stack-based
     * undo operations and a complete audit trail.
     *
     * Design notes:
     *  Rows are NEVER updated or deleted (append-only immutability).
     *  parent_event_id links UNDO events back to the event they reverse.
     *  payload_before / payload_after store JSON snapshots of the affected
     *  record before and after the action, enabling full undo/redo replay.
     *  stack_depth mirrors the in-memory undo-stack depth at the time of
     *  the event, useful for debugging undo/redo history.
     *
     * Columns
     *   event_id        – Surrogate auto-increment primary key.
     *   event_type      – Verb: 'INSERT', 'UPDATE', 'DELETE', 'UNDO',
     *                     'ROUTE_CALCULATED', 'REQUEST_ASSIGNED', …
     *   table_name      – The affected table (nullable for system events).
     *   record_id       – PK of the affected row as TEXT (nullable).
     *   parent_event_id – FK to audit_events.event_id for UNDO links.
     *   actor           – Free-text identifier of who triggered the event.
     *   payload_before  – JSON snapshot of the row before the change.
     *   payload_after   – JSON snapshot of the row after the change.
     *   stack_depth     – Undo-stack depth at time of event.
     *   occurred_at     – Precise timestamp of the event.
     */
    private static final String CREATE_AUDIT_EVENTS = ""
            + "CREATE TABLE IF NOT EXISTS audit_events (\n"
            + "    event_id        INTEGER  NOT NULL,\n"
            + "    event_type      TEXT     NOT NULL,\n"
            + "    table_name      TEXT,\n"
            + "    record_id       TEXT,\n"
            + "    parent_event_id INTEGER,\n"
            + "    actor           TEXT,\n"
            + "    payload_before  TEXT,\n"
            + "    payload_after   TEXT,\n"
            + "    stack_depth     INTEGER  NOT NULL DEFAULT 0\n"
            + "                            CHECK (stack_depth >= 0),\n"
            + "    occurred_at     TEXT     NOT NULL\n"
            + "                            DEFAULT (strftime('%Y-%m-%dT%H:%M:%fZ','now')),\n"
            + "\n"
            + "    CONSTRAINT pk_audit_events PRIMARY KEY (event_id AUTOINCREMENT),\n"
            + "    CONSTRAINT fk_ae_parent    FOREIGN KEY (parent_event_id)\n"
            + "                               REFERENCES audit_events (event_id)\n"
            + "                               ON DELETE RESTRICT\n"
            + ");";

    // ══════════════════════════════════════════════════════════════════════
    //  Indexes – improve query performance for common access patterns
    // ══════════════════════════════════════════════════════════════════════

    /** Speeds up edge look-ups by source node (used heavily by Dijkstra). */
    private static final String IDX_ROADS_SOURCE =
            "CREATE INDEX IF NOT EXISTS idx_roads_source "
            + "ON roads (source_id);";

    /** Speeds up edge look-ups by destination node. */
    private static final String IDX_ROADS_DEST =
            "CREATE INDEX IF NOT EXISTS idx_roads_destination "
            + "ON roads (destination_id);";

    /** Speeds up priority-queue ordering of service requests. */
    private static final String IDX_SR_PRIORITY =
            "CREATE INDEX IF NOT EXISTS idx_sr_priority "
            + "ON service_requests (priority DESC, requested_at ASC);";

    /** Speeds up filtering open (non-completed) requests by status. */
    private static final String IDX_SR_STATUS =
            "CREATE INDEX IF NOT EXISTS idx_sr_status "
            + "ON service_requests (status);";

    /** Speeds up resource availability queries. */
    private static final String IDX_RES_STATUS =
            "CREATE INDEX IF NOT EXISTS idx_res_status "
            + "ON resources (status);";

    /** Speeds up audit log queries by event type (e.g. find all UNDOs). */
    private static final String IDX_AE_TYPE =
            "CREATE INDEX IF NOT EXISTS idx_ae_event_type "
            + "ON audit_events (event_type);";

    /** Speeds up chronological audit log scans (newest-first). */
    private static final String IDX_AE_OCCURRED =
            "CREATE INDEX IF NOT EXISTS idx_ae_occurred_at "
            + "ON audit_events (occurred_at DESC);";

    // ══════════════════════════════════════════════════════════════════════
    //  Entry point
    // ══════════════════════════════════════════════════════════════════════

    /**
     * Prints the full DDL schema to stdout.
     *
     * No JDBC driver or external JAR is required.
     * Copy the output and paste it into DB Browser for SQLite, DBeaver,
     * or any SQL tool to create the schema.
     *
     * Compile:  javac -d build app\DatabaseSchema.java
     * Run:      java  -cp build app.DatabaseSchema
     */
    public static void main(String[] args) {
        System.out.println("-- ==============================================================");
        System.out.println("-- Innovative-Minds Campus Routing System");
        System.out.println("-- SQL Schema  (SQLite)");
        System.out.println("-- Generated by app.DatabaseSchema");
        System.out.println("-- Paste this output into DB Browser for SQLite or any SQL tool");
        System.out.println("-- ==============================================================");
        System.out.println();

        // Foreign-key enforcement for SQLite
        printSection("Pragma");
        System.out.println("PRAGMA foreign_keys = ON;");
        System.out.println();

        // Tables – dependency order:
        //   locations first (referenced by roads, service_requests,
        //                    resources, algorithm_runs)
        //   resources before service_requests
        //   (service_requests.assigned_to -> resources)
        printSection("Table: locations");
        System.out.println(CREATE_LOCATIONS);
        System.out.println();

        printSection("Table: resources");
        System.out.println(CREATE_RESOURCES);
        System.out.println();

        printSection("Table: roads");
        System.out.println(CREATE_ROADS);
        System.out.println();

        printSection("Table: service_requests");
        System.out.println(CREATE_SERVICE_REQUESTS);
        System.out.println();

        printSection("Table: algorithm_runs");
        System.out.println(CREATE_ALGORITHM_RUNS);
        System.out.println();

        printSection("Table: audit_events");
        System.out.println(CREATE_AUDIT_EVENTS);
        System.out.println();

        // Indexes
        printSection("Indexes");
        System.out.println(IDX_ROADS_SOURCE);
        System.out.println(IDX_ROADS_DEST);
        System.out.println(IDX_SR_PRIORITY);
        System.out.println(IDX_SR_STATUS);
        System.out.println(IDX_RES_STATUS);
        System.out.println(IDX_AE_TYPE);
        System.out.println(IDX_AE_OCCURRED);
        System.out.println();

        System.out.println("-- Schema complete. 6 tables, 7 indexes.");
    }

    // ──────────────────────────────────────────────────────────────────────
    //  Helper
    // ──────────────────────────────────────────────────────────────────────

    /** Prints a SQL comment banner for a section. */
    private static void printSection(String title) {
        System.out.println("-- ── " + title + " " + "─".repeat(Math.max(0, 50 - title.length())));
    }
}
