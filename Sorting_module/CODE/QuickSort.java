/**
 * Custom Quick Sort (Lomuto partition scheme with a randomized pivot to
 * avoid the O(n^2) worst case on already-sorted or duplicate-heavy data,
 * which is common with fields like "urgency" that only have 3 distinct
 * values).
 *
 * Time complexity : Best O(n log n) | Average O(n log n) | Worst O(n^2)
 * Space complexity: O(log n) recursion stack (in-place partitioning)
 * Stable          : No
 *
 * Good for fast average-case in-memory sorting where stability is not
 * required, e.g. sorting the full 300-record dataset for display.
 */
public class QuickSort {

    // TODO: replace with an actual Group B member's index number (e.g. the
    // last 6-8 digits) so this satisfies the brief's AI-resistance
    // requirement (Section 2.iii) to derive algorithm parameters from
    // member index numbers. Any fixed value works; this one is a placeholder.
    private static final long PIVOT_SEED = 10920123L;

    private static final MyRandom RANDOM = new MyRandom(PIVOT_SEED); // fixed seed = reproducible benchmarks

    public static void sort(ServiceRequest[] requests, MyComparator comparator) {
        if (requests.length < 2) return;
        quickSort(requests, 0, requests.length - 1, comparator);
    }

    private static void quickSort(ServiceRequest[] arr, int low, int high,
                                  MyComparator comparator) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high, comparator);
            quickSort(arr, low, pivotIndex - 1, comparator);
            quickSort(arr, pivotIndex + 1, high, comparator);
        }
    }

    private static int partition(ServiceRequest[] arr, int low, int high,
                                 MyComparator comparator) {
        // Randomized pivot selection, then swap into the last slot (Lomuto scheme)
        int randomIndex = low + RANDOM.nextInt(high - low + 1);
        swap(arr, randomIndex, high);

        ServiceRequest pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(arr[j], pivot) <= 0) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private static void swap(ServiceRequest[] arr, int a, int b) {
        ServiceRequest temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }
}
