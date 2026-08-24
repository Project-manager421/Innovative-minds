package campus;

/**
 * Custom Binary Search Tree for the assessed BST portion.
 * No Java collection class is used.
 */
public class BST {
    private static class Node {
        String key;
        ServiceRequest value;
        Node left;
        Node right;

        Node(String key, ServiceRequest value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size;

    public int size() { return size; }
    public boolean isEmpty() { return root == null; }

    public void insert(String key, ServiceRequest value) {
        if (root == null) {
            root = new Node(key, value);
            size++;
            return;
        }

        Node current = root;
        while (true) {
            int comparison = key.compareTo(current.key);
            if (comparison == 0) {
                current.value = value;
                return;
            }
            if (comparison < 0) {
                if (current.left == null) {
                    current.left = new Node(key, value);
                    size++;
                    return;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node(key, value);
                    size++;
                    return;
                }
                current = current.right;
            }
        }
    }

    public ServiceRequest search(String key) {
        Node current = root;
        while (current != null) {
            int comparison = key.compareTo(current.key);
            if (comparison == 0) return current.value;
            current = comparison < 0 ? current.left : current.right;
        }
        return null;
    }

    public boolean delete(String key) {
        Node parent = null;
        Node current = root;

        while (current != null && !key.equals(current.key)) {
            parent = current;
            current = key.compareTo(current.key) < 0 ? current.left : current.right;
        }

        if (current == null) return false;

        if (current.left != null && current.right != null) {
            Node successorParent = current;
            Node successor = current.right;
            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }
            current.key = successor.key;
            current.value = successor.value;
            parent = successorParent;
            current = successor;
        }

        Node child = current.left != null ? current.left : current.right;
        if (parent == null) root = child;
        else if (parent.left == current) parent.left = child;
        else parent.right = child;

        size--;
        return true;
    }

    public int height() {
        if (root == null) return -1;

        Node[] currentLevel = new Node[1];
        currentLevel[0] = root;
        int height = -1;

        while (currentLevel.length > 0) {
            height++;
            int childCount = 0;
            for (int i = 0; i < currentLevel.length; i++) {
                if (currentLevel[i].left != null) childCount++;
                if (currentLevel[i].right != null) childCount++;
            }

            Node[] nextLevel = new Node[childCount];
            int position = 0;
            for (int i = 0; i < currentLevel.length; i++) {
                if (currentLevel[i].left != null) nextLevel[position++] = currentLevel[i].left;
                if (currentLevel[i].right != null) nextLevel[position++] = currentLevel[i].right;
            }
            currentLevel = nextLevel;
        }
        return height;
    }

    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(Node node) {
        if (node == null) return;
        printInOrder(node.left);
        System.out.println(node.value);
        printInOrder(node.right);
    }
}
