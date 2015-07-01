import java.math.BigInteger;

public class BigIntegerInterpolator implements Interpolator<BigInteger>
{
    private static final BigIntegerInterpolator INSTANCE = new BigIntegerInterpolator();

    private BigIntegerInterpolator() { }

    public static BigIntegerInterpolator getInstance()
    {
        return INSTANCE;
    }

    public BigInteger interpolate(BigInteger low, BigInteger high)
    {
        if (low.compareTo(high) >= 0)
        {
            return null;
        }
        BigInteger mid = high.add(low).shiftRight(1);
        if (mid.equals(low) || mid.equals(high))
        {
            return null;
        }
        return mid;
    }
}
