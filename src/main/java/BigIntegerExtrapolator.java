import java.math.BigInteger;

public class BigIntegerExtrapolator implements Extrapolator<BigInteger>
{
    private static final BigIntegerExtrapolator INSTANCE = new BigIntegerExtrapolator();

    private BigIntegerExtrapolator() { }

    public static BigIntegerExtrapolator getInstance()
    {
        return INSTANCE;
    }

    public BigInteger extrapolateLow(BigInteger low, BigInteger high)
    {
        return low.subtract(high.subtract(low).shiftLeft(1));
    }

    public BigInteger extrapolateHigh(BigInteger low, BigInteger high)
    {
        return high.add(high.subtract(low).shiftLeft(1));
    }
}
