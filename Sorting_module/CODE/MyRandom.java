/**
 * Group B - Sorting Module
 *
 * Self-written 48-bit linear congruential generator that replaces
 * java.util.Random. No java.util import is used anywhere in this file -
 * only plain arithmetic. Deterministic given a seed, so benchmark runs
 * stay reproducible, which is required for the empirical efficiency
 * study (Section 9 of the project brief).
 */
public class MyRandom {

    // Same LCG constants described in Knuth's TAOCP Vol. 2, Sec. 3.2.1 -
    // reimplemented independently here, not imported from any library.
    private static final long MULTIPLIER = 0x5DEECE66DL;
    private static final long INCREMENT = 0xBL;
    private static final long MASK = (1L << 48) - 1;

    private long seed;

    public MyRandom(long seed) {
        this.seed = (seed ^ MULTIPLIER) & MASK;
    }

    private int next(int bits) {
        seed = (seed * MULTIPLIER + INCREMENT) & MASK;
        return (int) (seed >>> (48 - bits));
    }

    /** Returns a pseudo-random, evenly distributed int in [0, bound). */
    public int nextInt(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("bound must be positive");
        }

        if ((bound & -bound) == bound) { // bound is a power of 2
            return (int) ((bound * (long) next(31)) >> 31);
        }

        int bits, val;
        do {
            bits = next(31);
            val = bits % bound;
        } while (bits - val + (bound - 1) < 0);
        return val;
    }
}
