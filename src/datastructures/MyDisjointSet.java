package datastructures;

public class MyDisjointSet {

    private int[] parent;
    private int[] rank;

    public MyDisjointSet(int size) {

        parent = new int[size];
        rank = new int[size];

        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int value) {

        if (parent[value] != value) {
            parent[value] = find(parent[value]);
        }

        return parent[value];
    }

    public boolean union(int first, int second) {

        int rootFirst = find(first);
        int rootSecond = find(second);

        if (rootFirst == rootSecond) {
            return false;
        }

        if (rank[rootFirst] < rank[rootSecond]) {

            parent[rootFirst] = rootSecond;

        } else if (rank[rootFirst] > rank[rootSecond]) {

            parent[rootSecond] = rootFirst;

        } else {

            parent[rootSecond] = rootFirst;
            rank[rootFirst]++;
        }

        return true;
    }
}