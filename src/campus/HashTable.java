package campus;

/**
 * Custom hash table using an array of custom chained entries.
 * Collision handling is separate chaining. No Java collection class is used.
 */
public class HashTable {
    private static class Entry {
        String key;
        ServiceRequest value;
        Entry next;

        Entry(String key, ServiceRequest value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Entry[] buckets;
    private int size;
    private int collisions;
    private static final double LOAD_LIMIT = 0.75;

    public HashTable() {
        buckets = new Entry[16];
    }

    private int hash(String key) {
        long value = 0;
        for (int i = 0; i < key.length(); i++) {
            value = (value * 31 + key.charAt(i)) & 0x7fffffffL;
        }
        return (int)(value % buckets.length);
    }

    public void insert(String key, ServiceRequest value) {
        if ((double) size / buckets.length >= LOAD_LIMIT) resize();

        int index = hash(key);
        Entry current = buckets[index];
        if (current != null) collisions++;

        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        buckets[index] = new Entry(key, value, buckets[index]);
        size++;
    }

    public ServiceRequest search(String key) {
        int index = hash(key);
        Entry current = buckets[index];
        while (current != null) {
            if (current.key.equals(key)) return current.value;
            current = current.next;
        }
        return null;
    }

    public boolean delete(String key) {
        int index = hash(key);
        Entry current = buckets[index];
        Entry previous = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (previous == null) buckets[index] = current.next;
                else previous.next = current.next;
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    private void resize() {
        Entry[] oldBuckets = buckets;
        buckets = new Entry[oldBuckets.length * 2];
        size = 0;

        for (int i = 0; i < oldBuckets.length; i++) {
            Entry current = oldBuckets[i];
            while (current != null) {
                insert(current.key, current.value);
                current = current.next;
            }
        }
    }

    public int size() { return size; }
    public int capacity() { return buckets.length; }
    public double loadFactor() { return (double) size / buckets.length; }
    public int getCollisionCount() { return collisions; }

    public int maxChainLength() {
        int maximum = 0;
        for (int i = 0; i < buckets.length; i++) {
            int length = 0;
            Entry current = buckets[i];
            while (current != null) {
                length++;
                current = current.next;
            }
            if (length > maximum) maximum = length;
        }
        return maximum;
    }
}
