# UG Map Route – Updated Routing Submission

## What was updated

- Replaced the original 19-location routing dataset with the supplied **50-location University of Ghana dataset**.
- Added all **100 supplied road records** from the team dataset.
- Converted the supplied distances from kilometres to metres before storing them in `Extent`, so Dijkstra works with integer metre weights.
- Added the supplied travel-time values to each road.
- Kept the map bidirectional: each supplied road is represented in both directions internally when the reverse edge does not already exist.
- Updated the source and destination drop-downs on both the main route page and landmark page to use all 50 locations.
- Reworked the core graph and Dijkstra implementation so it does not use `java.util.ArrayList`, `HashMap`, `Set`, or other `java.util` collections for the assessed routing logic.
- Removed the unused `EdgeTimeComparator.java`, which depended on `java.util.Comparator`.
- Fixed repeated Dijkstra runs by resetting the algorithm state for every route calculation.
- Added explicit handling for unavailable routes.
- Added `dataset/locations.csv` and `dataset/roads.csv` for submission/evidence.

## Core imports

The assessed graph and shortest-path classes now use no `java.util` collection imports. The GUI classes still import Swing/AWT/image/file classes because those libraries are required to render the existing graphical interface and load the campus image.

## Dataset counts

- Locations: 50
- Supplied roads: 100
- Internal directed edges after bidirectional expansion: may be less than 200 when the supplied dataset already contains a reverse road; duplicate reverse edges are not stored twice.

## Distance unit

The source dataset specifies road distance in kilometres. The program converts each value using:

`distanceMetres = round(distanceKm * 1000)`

For example, `0.35 km` becomes `350 m`.

## Running

From the project root, compile:

```text
javac -d build app/*.java
```

Then run:

```text
java -cp build app.Main
```

The project uses Java Swing for the existing graphical interface.
