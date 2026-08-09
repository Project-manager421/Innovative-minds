import java.io.FileWriter;
import java.io.IOException;

/**

 * Benchmark harness that:
 *   1. Verifies correctness of InsertionSort, MergeSort and QuickSort by
 *      checking (a) the output is fully sorted under the given rule, and
 *      (b) it is a permutation of the input (no request lost or duplicated).
 *      No built-in oracle (e.g. Arrays.sort) is used - java.io is the only
 *      built-in import here, per Section 8.ii of the project brief.
 *   2. Measures real execution time (System.nanoTime) as dataset size grows,
 *      using the exact input sizes required by Section 9 of the brief
 *      (100, 500, 1,000, 5,000, 10,000 requests).
 *   3. Stress-tests QuickSort against heavy-duplicate data (urgency has only
 *      3 distinct values) versus MergeSort/InsertionSort.
 *   4. Writes results to CSV so they can be charted in the report.
 *
 * Source/destination on every generated request are real location names
 * loaded once from the team's edges CSV via LocationLoader, not synthetic
 * IDs - see loadLocationNames() below.
 */
public class SortPerformanceTest {

    private static final String EDGES_CSV_PATH = "innovative-minds-DATASET-edges.csv";

    public static void main(String[] args) throws IOException {
        System.out.println("=== Group B: Sorting Module Benchmark ===\n");

        String[] locationNames = LocationLoader.loadLocationNames(EDGES_CSV_PATH);
        System.out.println("Loaded " + locationNames.length + " distinct locations from " + EDGES_CSV_PATH + "\n");

        correctnessCheck(locationNames);
        System.out.println();

        StringBuilder csv = new StringBuilder("Algorithm,SortKey,DatasetSize,TimeMillis\n");

        System.out.println("--- Growth benchmark: sort by Submission Time (unique values) ---");
        int[] sizes = {100, 500, 1000, 5000, 10000};
        runGrowthBenchmark(sizes, RequestComparators.BY_SUBMISSION_TIME, "SubmissionTime", csv, locationNames);

        System.out.println("\n--- Duplicate-heavy stress test: sort by Urgency (only 3 distinct values), n=5000 ---");
        runDuplicateStressTest(5000, csv, locationNames);

        System.out.println("\n--- Best-case check: Insertion Sort on a nearly-sorted list, n=2000 ---");
        runNearlySortedCheck(2000, csv, locationNames);

        try (FileWriter fw = new FileWriter("sortperformancetest_results.csv")) {
            fw.write(csv.toString());
        }
        System.out.println("\nResults written to sortperformancetest_results.csv");
    }

    // ---------------------------------------------------------------
    // 1. Correctness check
    // ---------------------------------------------------------------
    private static void correctnessCheck(String[] locationNames) {
        System.out.println("--- Correctness check (100 random requests, all 3 sort keys) ---");
        ServiceRequest[] base = DataGenerator.generate(100, 7, locationNames);

        MyComparator[] comparators = {
            RequestComparators.BY_URGENCY,
            RequestComparators.BY_SUBMISSION_TIME,
            RequestComparators.BY_DEADLINE
        };
        String[] names = {"Urgency", "SubmissionTime", "Deadline"};

        for (int c = 0; c < comparators.length; c++) {
            MyComparator comparator = comparators[c];

            ServiceRequest[] a = SortUtils.copyOf(base);
            ServiceRequest[] b = SortUtils.copyOf(base);
            ServiceRequest[] d = SortUtils.copyOf(base);

            InsertionSort.sort(a, comparator);
            MergeSort.sort(b, comparator);
            QuickSort.sort(d, comparator);

            boolean insertionOk = SortUtils.isSorted(a, comparator) && SortUtils.isPermutation(base, a);
            boolean mergeOk = SortUtils.isSorted(b, comparator) && SortUtils.isPermutation(base, b);
            boolean quickOk = SortUtils.isSorted(d, comparator) && SortUtils.isPermutation(base, d);

            System.out.printf("  Sort key = %-15s | InsertionSort: %-4s | MergeSort: %-4s | QuickSort: %-4s%n",
                    names[c], insertionOk ? "PASS" : "FAIL", mergeOk ? "PASS" : "FAIL", quickOk ? "PASS" : "FAIL");
        }
    }

    // ---------------------------------------------------------------
    // 2. Growth benchmark
    // ---------------------------------------------------------------
    private static void runGrowthBenchmark(int[] sizes, MyComparator comparator,
                                            String keyName, StringBuilder csv, String[] locationNames) {
        System.out.printf("%-8s | %-14s | %-12s | %-12s%n", "n", "InsertionSort", "MergeSort", "QuickSort");
        for (int n : sizes) {
            ServiceRequest[] original = DataGenerator.generate(n, 123, locationNames);

            double insertionMs = timeSort(original, comparator, InsertionSort::sort);
            double mergeMs = timeSort(original, comparator, MergeSort::sort);
            double quickMs = timeSort(original, comparator, QuickSort::sort);

            System.out.printf("%-8d | %-14.3f | %-12.3f | %-12.3f%n", n, insertionMs, mergeMs, quickMs);

            csv.append("InsertionSort,").append(keyName).append(",").append(n).append(",").append(insertionMs).append("\n");
            csv.append("MergeSort,").append(keyName).append(",").append(n).append(",").append(mergeMs).append("\n");
            csv.append("QuickSort,").append(keyName).append(",").append(n).append(",").append(quickMs).append("\n");
        }
    }

    // ---------------------------------------------------------------
    // 3. Duplicate-heavy stress test (urgency only has 3 distinct values)
    // ---------------------------------------------------------------
    private static void runDuplicateStressTest(int n, StringBuilder csv, String[] locationNames) {
        ServiceRequest[] original = DataGenerator.generate(n, 55, locationNames);
        MyComparator comparator = RequestComparators.BY_URGENCY;

        double insertionMs = timeSort(original, comparator, InsertionSort::sort);
        double mergeMs = timeSort(original, comparator, MergeSort::sort);
        double quickMs = timeSort(original, comparator, QuickSort::sort);

        System.out.printf("%-8s | %-14.3f | %-12.3f | %-12.3f%n", n + "", insertionMs, mergeMs, quickMs);

        csv.append("InsertionSort,Urgency(dup-heavy),").append(n).append(",").append(insertionMs).append("\n");
        csv.append("MergeSort,Urgency(dup-heavy),").append(n).append(",").append(mergeMs).append("\n");
        csv.append("QuickSort,Urgency(dup-heavy),").append(n).append(",").append(quickMs).append("\n");
    }

    // ---------------------------------------------------------------
    // 4. Nearly-sorted best-case check for Insertion Sort
    // ---------------------------------------------------------------
    private static void runNearlySortedCheck(int n, StringBuilder csv, String[] locationNames) {
        ServiceRequest[] sorted = DataGenerator.generate(n, 99, locationNames);
        // Build the "already sorted" fixture with our own MergeSort rather
        // than a built-in oracle - keeps java.util out of the harness entirely.
        MergeSort.sort(sorted, RequestComparators.BY_SUBMISSION_TIME);

        // Randomly perturb ~2% of elements so it's "nearly" sorted, not perfectly sorted
        MyRandom rand = new MyRandom(3);
        for (int i = 0; i < n / 50; i++) {
            int x = rand.nextInt(n), y = rand.nextInt(n);
            ServiceRequest tmp = sorted[x];
            sorted[x] = sorted[y];
            sorted[y] = tmp;
        }

        MyComparator comparator = RequestComparators.BY_SUBMISSION_TIME;
        double insertionMs = timeSort(sorted, comparator, InsertionSort::sort);
        double mergeMs = timeSort(sorted, comparator, MergeSort::sort);
        double quickMs = timeSort(sorted, comparator, QuickSort::sort);

        System.out.printf("%-8s | %-14.3f | %-12.3f | %-12.3f%n", n + "", insertionMs, mergeMs, quickMs);
        System.out.println("  (Insertion Sort approaches its O(n) best case on nearly-sorted data)");

        csv.append("InsertionSort,NearlySorted,").append(n).append(",").append(insertionMs).append("\n");
        csv.append("MergeSort,NearlySorted,").append(n).append(",").append(mergeMs).append("\n");
        csv.append("QuickSort,NearlySorted,").append(n).append(",").append(quickMs).append("\n");
    }

    // ---------------------------------------------------------------
    // Timing helper - averages several trials on a fresh copy each time
    // ---------------------------------------------------------------
    private static double timeSort(ServiceRequest[] original, MyComparator comparator, SortRoutine sortFn) {
        int trials = original.length <= 2000 ? 7 : 3;
        long totalNanos = 0;

        for (int t = 0; t < trials; t++) {
            ServiceRequest[] copy = SortUtils.copyOf(original);
            long start = System.nanoTime();
            sortFn.sort(copy, comparator);
            long end = System.nanoTime();
            totalNanos += (end - start);
        }
        return (totalNanos / (double) trials) / 1_000_000.0; // avg milliseconds
    }
}
