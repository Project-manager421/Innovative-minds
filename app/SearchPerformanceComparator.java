package app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SearchPerformanceComparator {

    private final LinearSearch<ServiceRequest, String> linearSearch =
            LinearSearch.forComparableKey(ServiceRequest::getRequestId);
    private final BinarySearch<ServiceRequest, String> binarySearch =
            BinarySearch.forComparableKey(ServiceRequest::getRequestId);

    public static class Result {
        public final int datasetSize;
        public final long linearNanos;
        public final long binaryNanos;

        public Result(int datasetSize, long linearNanos, long binaryNanos) {
            this.datasetSize = datasetSize;
            this.linearNanos = linearNanos;
            this.binaryNanos = binaryNanos;
        }

        @Override
        public String toString() {
            return String.format(
                    "n=%-7d | linear=%,10d ns | binary=%,8d ns | ratio=%.1fx",
                    datasetSize, linearNanos, binaryNanos,
                    binaryNanos == 0 ? 0 : (double) linearNanos / binaryNanos);
        }
    }

    public List<ServiceRequest> generateDataset(int size) {
        List<ServiceRequest> data = new ArrayList<>(size);
        String[] locationNames = {
                "Balme Library", "Computer Science Department", "Great Hall", "UG Hospital",
                "Legon Hall", "Akuafo Hall", "Night Market", "Main Gate", "Registry",
                "School of Engineering Sciences"
        };
        String[] categories = {"IT Support", "Electrical", "Plumbing", "Cleaning",
                "Security", "Transport", "Facility Maintenance"};
        UrgencyLevel[] urgencies = UrgencyLevel.values();
        Random random = new Random(42);

        for (int i = 0; i < size; i++) {
            String id = String.format("SR%06d", i);
            data.add(new ServiceRequest(
                    id,
                    categories[random.nextInt(categories.length)],
                    "Generated request",
                    "L" + String.format("%03d", 1 + random.nextInt(50)),
                    locationNames[random.nextInt(locationNames.length)],
                    urgencies[random.nextInt(urgencies.length)],
                    RequestStatus.PENDING,
                    "2026-01-06 07:30"));
        }
        return data;
    }

    public Result timeSingleRun(List<ServiceRequest> sortedData, String targetKey) {
        long linearStart = System.nanoTime();
        linearSearch.findFirst(sortedData, targetKey);
        long linearElapsed = System.nanoTime() - linearStart;

        long binaryStart = System.nanoTime();
        binarySearch.find(sortedData, targetKey);
        long binaryElapsed = System.nanoTime() - binaryStart;

        return new Result(sortedData.size(), linearElapsed, binaryElapsed);
    }

    public List<Result> runComparison(int... datasetSizes) {
        List<Result> results = new ArrayList<>();
        for (int size : datasetSizes) {
            List<ServiceRequest> data = generateDataset(size);
            data.sort(Comparator.comparing(ServiceRequest::getRequestId));
            String missingKey = "SR999999";
            results.add(timeSingleRun(data, missingKey));
        }
        return results;
    }
}
