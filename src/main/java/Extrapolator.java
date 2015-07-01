public interface Extrapolator<T>
{
    /**
     * Returns a value v such that v < low.
     *
     * If no such v exists, returns null.
     */
    T extrapolateLow(T low, T high);

    /**
     * Returns a value v such that v > high.
     *
     * If no such v exists, returns null.
     */
    T extrapolateHigh(T low, T high);
}
