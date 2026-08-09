package app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class LinearSearch<T, K> {

    private final KeyExtractor<T, K> keyExtractor;
    private final Comparator<K> keyComparator;

    public interface KeyExtractor<T, K> {
        K extractKey(T element);
    }

    public LinearSearch(KeyExtractor<T, K> keyExtractor, Comparator<K> keyComparator) {
        this.keyExtractor = keyExtractor;
        this.keyComparator = keyComparator;
    }

    public static <T, K extends Comparable<K>> LinearSearch<T, K> forComparableKey(
            KeyExtractor<T, K> keyExtractor) {
        return new LinearSearch<>(keyExtractor, Comparator.naturalOrder());
    }

    public int findIndex(List<T> data, K targetKey) {
        if (data == null || targetKey == null) {
            return -1;
        }
        for (int i = 0; i < data.size(); i++) {
            K currentKey = keyExtractor.extractKey(data.get(i));
            if (currentKey != null && keyComparator.compare(currentKey, targetKey) == 0) {
                return i;
            }
        }
        return -1;
    }

    public T findFirst(List<T> data, K targetKey) {
        int index = findIndex(data, targetKey);
        return index == -1 ? null : data.get(index);
    }

    public List<T> findAll(List<T> data, K targetKey) {
        List<T> results = new ArrayList<>();
        if (data == null || targetKey == null) {
            return results;
        }
        for (T element : data) {
            K currentKey = keyExtractor.extractKey(element);
            if (currentKey != null && keyComparator.compare(currentKey, targetKey) == 0) {
                results.add(element);
            }
        }
        return results;
    }
}
