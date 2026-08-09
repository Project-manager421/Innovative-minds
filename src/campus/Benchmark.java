package campus;

/**
 * Performance comparison for BST and Hash Table.
 * Each execution creates a new CSV file, so every run has its own evidence.
 */
public class Benchmark {
    private static final int[] SIZES = {100, 500, 1000, 5000, 10000};
    private static final int TRIALS = 3;

    public static String run(String outputDirectory) throws Exception {
        java.io.File directory = new java.io.File(outputDirectory);
        if (!directory.exists()) directory.mkdirs();

        String fileName = outputDirectory + "/run_" + System.currentTimeMillis() + ".csv";
        java.io.FileWriter writer = new java.io.FileWriter(fileName);
        writer.write("inputSize,order,operation,bstTimeNs,hashTableTimeNs,bstHeight,hashCollisions,hashMaxChain\n");

        for (int s = 0; s < SIZES.length; s++) {
            int n = SIZES[s];
            for (int orderType = 0; orderType < 2; orderType++) {
                String[] keys = createKeys(n, orderType == 0);
                String[] searchKeys = createSearchKeys(n);
                String[] deleteKeys = createDeleteKeys(n);

                long bstInsert = 0, bstSearch = 0, bstDelete = 0;
                long hashInsert = 0, hashSearch = 0, hashDelete = 0;
                int bstHeight = 0, hashCollisions = 0, hashMaxChain = 0;

                for (int trial = 0; trial < TRIALS; trial++) {
                    BST bst = new BST();
                    long start = System.nanoTime();
                    for (int i = 0; i < n; i++) bst.insert(keys[i], request(keys[i]));
                    bstInsert += System.nanoTime() - start;
                    bstHeight = bst.height();

                    start = System.nanoTime();
                    for (int i = 0; i < searchKeys.length; i++) bst.search(searchKeys[i]);
                    bstSearch += System.nanoTime() - start;

                    start = System.nanoTime();
                    for (int i = 0; i < deleteKeys.length; i++) bst.delete(deleteKeys[i]);
                    bstDelete += System.nanoTime() - start;

                    HashTable table = new HashTable();
                    start = System.nanoTime();
                    for (int i = 0; i < n; i++) table.insert(keys[i], request(keys[i]));
                    hashInsert += System.nanoTime() - start;

                    start = System.nanoTime();
                    for (int i = 0; i < searchKeys.length; i++) table.search(searchKeys[i]);
                    hashSearch += System.nanoTime() - start;

                    start = System.nanoTime();
                    for (int i = 0; i < deleteKeys.length; i++) table.delete(deleteKeys[i]);
                    hashDelete += System.nanoTime() - start;

                    hashCollisions = table.getCollisionCount();
                    hashMaxChain = table.maxChainLength();
                }

                bstInsert /= TRIALS; bstSearch /= TRIALS; bstDelete /= TRIALS;
                hashInsert /= TRIALS; hashSearch /= TRIALS; hashDelete /= TRIALS;

                writeRow(writer, n, orderType == 0 ? "random" : "sorted", "insert", bstInsert, hashInsert, bstHeight, hashCollisions, hashMaxChain);
                writeRow(writer, n, orderType == 0 ? "random" : "sorted", "search", bstSearch, hashSearch, bstHeight, hashCollisions, hashMaxChain);
                writeRow(writer, n, orderType == 0 ? "random" : "sorted", "delete", bstDelete, hashDelete, bstHeight, hashCollisions, hashMaxChain);
            }
        }

        writer.close();
        return fileName;
    }

    private static void writeRow(java.io.FileWriter writer, int n, String order, String operation,
                                 long bst, long hash, int height, int collisions, int maxChain) throws Exception {
        writer.write(n + "," + order + "," + operation + "," + bst + "," + hash + ","
                + height + "," + collisions + "," + maxChain + "\n");
    }

    private static String[] createKeys(int n, boolean randomOrder) {
        String[] keys = new String[n];
        for (int i = 0; i < n; i++) keys[i] = key(i + 1);
        if (randomOrder) shuffle(keys, 42 + n);
        return keys;
    }

    private static String[] createSearchKeys(int n) {
        int count = n < 200 ? n : 200;
        String[] keys = new String[count];
        for (int i = 0; i < count; i++) keys[i] = key(((i * 37) % n) + 1);
        return keys;
    }

    private static String[] createDeleteKeys(int n) {
        int count = n < 50 ? n : 50;
        String[] keys = new String[count];
        for (int i = 0; i < count; i++) keys[i] = key(((i * 19) % n) + 1);
        return keys;
    }

    private static void shuffle(String[] values, int seed) {
        int state = seed;
        for (int i = values.length - 1; i > 0; i--) {
            state = state * 1103515245 + 12345;
            int j = ((state & 0x7fffffff) % (i + 1));
            String temp = values[i]; values[i] = values[j]; values[j] = temp;
        }
    }

    private static String key(int number) {
        if (number < 10) return "SR000" + number;
        if (number < 100) return "SR00" + number;
        if (number < 1000) return "SR0" + number;
        return "SR" + number;
    }

    private static ServiceRequest request(String key) {
        return new ServiceRequest(key, "Maintenance", "Benchmark request", "L001",
                "Medium", "Pending", "09:00", "12:00", "");
    }
}
