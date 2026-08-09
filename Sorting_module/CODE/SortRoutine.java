/**
 * Replaces java.util.function.BiConsumer in the benchmark harness so that
 * InsertionSort::sort, MergeSort::sort and QuickSort::sort can be passed
 * around as values without importing anything from java.util.
 */
@FunctionalInterface
public interface SortRoutine {
    void sort(ServiceRequest[] requests, MyComparator comparator);
}
