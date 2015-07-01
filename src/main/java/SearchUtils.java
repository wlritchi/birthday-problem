import java.util.Comparator;
import java.util.function.Function;

public final class SearchUtils
{
    private SearchUtils() { }

    public static <K, V> K unboundedBinarySearch(Function<K, V> function, V needle, K low, K high, Comparator<K> keyComparator, Comparator<V> valueComparator, Interpolator<K> interpolator, Extrapolator<K> extrapolator)
    {
        if (keyComparator.compare(low, high) > 0)
        {
            K tmp = low;
            low = high;
            high = tmp;
        }

        while (true)
        {
            int cmp = valueComparator.compare(needle, function.apply(low));
            if (cmp < 0)
            {
                K tmp = extrapolator.extrapolateLow(low, high);
                high = low;
                low = tmp;
            }
            else if (cmp > 0)
            {
                break;
            }
            else
            {
                return low;
            }
        }

        while (true)
        {
            int cmp = valueComparator.compare(needle, function.apply(high));
            if (cmp > 0)
            {
                K tmp = extrapolator.extrapolateHigh(low, high);
                low = high;
                high = tmp;
            }
            else if (cmp < 0)
            {
                break;
            }
            else
            {
                return high;
            }
        }

        return binarySearch(function, needle, low, high, keyComparator, valueComparator, interpolator);
    }

    /**
     * Returns the key k with k > low, k <= high such that either cache.get(k) = needle,
     * or k is the insertion point for needle in a list derived from the cache.
     */
    public static <K, V> K binarySearch(Function<K, V> function, V needle, K low, K high, Comparator<K> keyComparator, Comparator<V> valueComparator, Interpolator<K> interpolator)
    {
        while (true)
        {
            K mid = interpolator.interpolate(low, high);
            if (mid == null)
            {
                return high;
            }
            V midVal = function.apply(mid);
            int cmp = valueComparator.compare(midVal, needle);
            if (cmp < 0)
            {
                low = mid;
            }
            else if (cmp > 0)
            {
                high = mid;
            }
            else
            {
                return mid;
            }
        }
    }
}
