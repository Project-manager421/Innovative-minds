/**
 * Small hand-written helpers that replace the java.util.Arrays calls the
 * original benchmark relied on (copyOf, sort-as-oracle). Keeping these
 * here means InsertionSort, MergeSort, QuickSort and the benchmark never
 * need to import java.util for their assessed logic.
 */
public class SortUtils {

    private SortUtils() {
        // utility class - no instances
    }

    /** Manual equivalent of Arrays.copyOf(source, source.length). */
    public static ServiceRequest[] copyOf(ServiceRequest[] source) {
        ServiceRequest[] copy = new ServiceRequest[source.length];
        for (int i = 0; i < source.length; i++) {
            copy[i] = source[i];
        }
        return copy;
    }

    /** True if every element is <= the next element under the comparator. */
    public static boolean isSorted(ServiceRequest[] arr, MyComparator comparator) {
        for (int i = 1; i < arr.length; i++) {
            if (comparator.compare(arr[i - 1], arr[i]) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * True if 'actual' contains exactly the same request IDs as 'original',
     * each the same number of times (i.e. sorting only reordered elements,
     * it never dropped or duplicated one). O(n^2), which is fine at the
     * correctness-check sizes this is used at (Section 10 requires
     * correctness evidence, not that the check itself be fast).
     */
    public static boolean isPermutation(ServiceRequest[] original, ServiceRequest[] actual) {
        if (original.length != actual.length) {
            return false;
        }
        boolean[] matched = new boolean[actual.length];
        for (ServiceRequest wanted : original) {
            boolean found = false;
            for (int j = 0; j < actual.length; j++) {
                if (!matched[j] && actual[j].getRequestId().equals(wanted.getRequestId())) {
                    matched[j] = true;
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }
}
