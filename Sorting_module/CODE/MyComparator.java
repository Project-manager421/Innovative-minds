/**
 * Group B - Sorting Module
 *
 * Custom replacement for java.util.Comparator. The project brief only
 * whitelists built-in Java utilities for file I/O, printing, JDBC and
 * unit-test scaffolding (Section 8.ii) - everything that touches the
 * assessed sorting logic, including the comparison contract itself,
 * is written from scratch here.
 */
@FunctionalInterface
public interface MyComparator {

    /**
     * @return negative if a should come before b, positive if a should
     *         come after b, 0 if they are equivalent for ordering purposes.
     */
    int compare(ServiceRequest a, ServiceRequest b);
}
