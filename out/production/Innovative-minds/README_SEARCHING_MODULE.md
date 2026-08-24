# Searching Module (Group A)

Members 2-4 responsibilities: Linear Search, Binary Search, Search by Request ID,
Search by Resource ID, Search by Location, performance comparison between Linear
and Binary Search.

## Files added (all in `app/`, same flat package as the rest of the project)

- `UrgencyLevel.java`, `RequestStatus.java` - enums
- `CampusLocation.java` - location record with ID, used only by the search module
  (kept separate from the existing `Locations` class used by the graph/routing code)
- `ServiceRequest.java`, `ServiceResource.java` - data models
- `LinearSearch.java`, `BinarySearch.java` - generic search algorithms, reused by
  every search service below
- `RequestSearchService.java` - Search by Request ID
- `ResourceSearchService.java` - Search by Resource ID
- `LocationSearchService.java` - Search by Location (locations, and requests/resources
  at a given location)
- `SearchDatasetLoader.java` - loads the CSVs in `dataset/` using plain `java.io`
- `SearchPerformanceComparator.java` - times linear vs binary search across dataset sizes
- `SearchDemo.java` - runnable demo, loads the real dataset and exercises every service
- `SearchModuleTest.java` - self-contained unit tests (no external dependencies)

## Dataset files added

- `dataset/service_requests.csv` - 300 generated requests linked to the real 50 locations
- `dataset/resources.csv` - generated resource pool linked to the real 50 locations

No `service_requests`/`resources` data existed yet, so these were generated against the
real `locations.csv` IDs and names so the search module has something concrete to run
against until live data comes from the database module.

## Compiling and running

From the project root:

```
javac -d build app/*.java
java -cp build app.SearchDemo
java -cp build app.SearchModuleTest
```

`SearchDemo` prints sample searches plus a linear-vs-binary performance table.
`SearchModuleTest` runs the unit tests and prints PASS/FAIL per test, exiting non-zero
if anything fails.
