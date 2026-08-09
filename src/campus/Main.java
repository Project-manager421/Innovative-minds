package campus;

/** Main demonstration for the BST + Hash Table maintenance module. */
public class Main {
    public static void main(String[] args) {
        String dataset = args.length > 0 ? args[0] : findPath("datasets/service_requests.csv");
        String results = args.length > 1 ? args[1] : findPath("results");

        System.out.println("=== UG Maintenance System ===");

        try {
            ServiceRequest[] requests = CsvLoader.loadServiceRequests(dataset);
            System.out.println("Loaded service requests: " + requests.length);

            demonstrateBST(requests);
            demonstrateHashTable(requests);

            String csv = Benchmark.run(results);
            System.out.println("\nPerformance CSV created: " + csv);
            System.out.println("Every execution creates a separate CSV file.");
        } catch (Exception e) {
            System.out.println("Program error: " + e.getMessage());
        }
    }

    private static String findPath(String relativePath) {
        String[] candidates = new String[] {
            relativePath,
            "../" + relativePath,
            "../../" + relativePath,
            "../../../" + relativePath
        };

        for (int i = 0; i < candidates.length; i++) {
            java.io.File file = new java.io.File(candidates[i]);
            if (file.exists()) {
                return candidates[i];
            }
        }
        return relativePath;
    }

    private static void demonstrateBST(ServiceRequest[] requests) {
        System.out.println("\n=== BINARY SEARCH TREE ===");
        BST bst = new BST();
        for (int i = 0; i < requests.length; i++) {
            bst.insert(requests[i].getRequestId(), requests[i]);
        }

        System.out.println("Inserted all " + bst.size() + " service requests.");
        System.out.println("Search SR150: " + bst.search("SR150"));
        System.out.println("Delete SR001: " + bst.delete("SR001"));
        System.out.println("Search SR001 after deletion: " + bst.search("SR001"));
        System.out.println("BST height after deletion: " + bst.height());
        System.out.println("The tree is highly skewed because RequestIDs are loaded in sorted order.");
    }

    private static void demonstrateHashTable(ServiceRequest[] requests) {
        System.out.println("\n=== HASH TABLE ===");
        HashTable table = new HashTable();
        for (int i = 0; i < requests.length; i++) {
            table.insert(requests[i].getRequestId(), requests[i]);
        }

        System.out.println("Inserted all " + table.size() + " service requests.");
        System.out.println("Search SR150: " + table.search("SR150"));
        System.out.println("Delete SR001: " + table.delete("SR001"));
        System.out.println("Search SR001 after deletion: " + table.search("SR001"));
        System.out.println("Capacity: " + table.capacity());
        System.out.println("Load factor: " + table.loadFactor());
        System.out.println("Collisions observed: " + table.getCollisionCount());
        System.out.println("Longest chain: " + table.maxChainLength());
    }

}
