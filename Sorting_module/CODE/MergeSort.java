/**
 * Custom Merge Sort (top-down, recursive, divide and conquer).
 *
 * Time complexity : Best O(n log n) | Average O(n log n) | Worst O(n log n)
 * Space complexity: O(n) (needs an auxiliary array for merging)
 * Stable          : Yes
 *
 * Good choice when we need predictable, guaranteed O(n log n) performance
 * regardless of how the incoming request batch is ordered, e.g. nightly
 * batch sorting of all 300 service requests.
 */
public class MergeSort {

    public static void sort(ServiceRequest[] requests, MyComparator comparator) {
        if (requests.length < 2) return;
        ServiceRequest[] buffer = new ServiceRequest[requests.length];
        mergeSort(requests, buffer, 0, requests.length - 1, comparator);
    }

    private static void mergeSort(ServiceRequest[] arr, ServiceRequest[] buffer,
                                  int left, int right, MyComparator comparator) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSort(arr, buffer, left, mid, comparator);
        mergeSort(arr, buffer, mid + 1, right, comparator);
        merge(arr, buffer, left, mid, right, comparator);
    }

    private static void merge(ServiceRequest[] arr, ServiceRequest[] buffer,
                              int left, int mid, int right, MyComparator comparator) {
        for (int i = left; i <= right; i++) {
            buffer[i] = arr[i];
        }

        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            if (comparator.compare(buffer[i], buffer[j]) <= 0) {
                arr[k++] = buffer[i++];
            } else {
                arr[k++] = buffer[j++];
            }
        }
        while (i <= mid) {
            arr[k++] = buffer[i++];
        }
        while (j <= right) {
            arr[k++] = buffer[j++];
        }
    }
}
