/**
 * Custom Insertion Sort.
 *
 * Time complexity : Best O(n)  | Average O(n^2) | Worst O(n^2)
 * Space complexity: O(1) (in-place)
 * Stable          : Yes
 *
 * Good for small or nearly-sorted request batches (e.g. a short queue of
 * newly arrived requests that only needs light re-ordering).
 */
public class InsertionSort {

    public static void sort(ServiceRequest[] requests, MyComparator comparator) {
        int n = requests.length;
        for (int i = 1; i < n; i++) {
            ServiceRequest key = requests[i];
            int j = i - 1;
            while (j >= 0 && comparator.compare(requests[j], key) > 0) {
                requests[j + 1] = requests[j];
                j--;
            }
            requests[j + 1] = key;
        }
    }
}
