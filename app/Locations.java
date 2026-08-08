package app;

public class Locations {
    private final String name;

    public Locations(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Locations) {
            Locations other = (Locations) obj;
            return other.getName().equalsIgnoreCase(name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    @Override
    public String toString() { return name; }
}
