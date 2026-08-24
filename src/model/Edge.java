package model;

public class Edge {

    private String id;
    private Vertex from;
    private Vertex to;
    private double distance;
    private int travelTime;

    public Edge(String id, Vertex from, Vertex to, double distance, int travelTime) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.travelTime = travelTime;
    }

    public String getId() {
        return id;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }

    public double getDistance() {
        return distance;
    }

    public int getTravelTime() {
        return travelTime;
    }

    @Override
    public String toString() {
        return id + ": " + from.getName() + " -> " 
                + to.getName() + " (" + travelTime + " min)";
    }
}