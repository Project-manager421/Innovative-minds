/**
 * Central place for the three comparison rules Group B needs to support:
 *   1. By urgency        (High -> Medium -> Low)
 *   2. By submission time (earliest first)
 *   3. By deadline        (soonest first)
 *
 * These are plain MyComparator objects (our own functional interface, not
 * java.util.Comparator), passed into our own InsertionSort / MergeSort /
 * QuickSort classes so the same three sorting algorithms can sort by any
 * of the three keys.
 */
public class RequestComparators {

    public static final MyComparator BY_URGENCY =
            (a, b) -> b.getUrgency().getWeight() - a.getUrgency().getWeight();

    public static final MyComparator BY_SUBMISSION_TIME =
            (a, b) -> a.getSubmissionTime().compareTo(b.getSubmissionTime());

    public static final MyComparator BY_DEADLINE =
            (a, b) -> a.getDeadline().compareTo(b.getDeadline());
}
