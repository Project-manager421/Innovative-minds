package app;

import java.util.Comparator;
import java.util.List;

public final class BinarySearch<T, K> {

    private final LinearSearch.KeyExtractor<T, K> keyExtractor;
    private final Comparator<K> keyComparator;

    public BinarySearch(LinearSearch.KeyExtractor<T, K> keyExtractor, Comparator<K> keyComparator) {
        this.keyExtractor = keyExtractor;
        this.keyComparator = keyComparator;
    }

    public static <T, K extends Comparable<K>> BinarySearch<T, K> forComparableKey(
            LinearSearch.KeyExtractor<T, K> keyExtractor) {
        return new BinarySearch<>(keyExtractor, Comparator.naturalOrder());
    }

    public int findIndex(List<T> data, K targetKey) {
        if (data == null || targetKey == null || data.isEmpty()) {
            return -1;
        }

        int low = 0;
        int high = data.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            K midKey = keyExtractor.extractKey(data.get(mid));
            int cmp = keyComparator.compare(midKey, targetKey);

            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    public T find(List<T> data, K targetKey) {
        int index = findIndex(data, targetKey);
        return index == -1 ? null : data.get(index);
    }

    public boolean isSorted(List<T> data) {
        for (int i = 1; i < data.size(); i++) {
            K prev = keyExtractor.extractKey(data.get(i - 1));
            K curr = keyExtractor.extractKey(data.get(i));
            if (keyComparator.compare(prev, curr) > 0) {
                return false;
            }
        }
        return true;
    }
}
