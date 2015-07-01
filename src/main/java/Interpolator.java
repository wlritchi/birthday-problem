public interface Interpolator<T>
{
    /**
     * Returns a value v such that low < v < high.
     *
     * If no such value v exists, returns null.
     */
    T interpolate(T low, T high);
}
