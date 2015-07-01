import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;

public class BigFract implements Comparable<BigFract>
{
    private static final BigInteger TWO = BigInteger.valueOf(2);

    public static final BigFract ZERO = new BigFract(0, 1);
    public static final BigFract ONE = new BigFract(1, 1);

    private final BigInteger numerator;
    private final BigInteger denominator;

    public BigFract(long numerator, long denominator)
    {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }
    public BigFract(BigInteger numerator, BigInteger denominator)
    {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public BigFract negate()
    {
        return new BigFract(numerator.negate(), denominator);
    }

    public BigFract reciprocal()
    {
        return new BigFract(denominator, numerator);
    }

    public BigFract add(long value)
    {
        return add(BigInteger.valueOf(value));
    }
    public BigFract add(BigInteger value)
    {
        return new BigFract(numerator.add(denominator.multiply(value)), denominator);
    }
    public BigFract add(BigFract value)
    {
        return new BigFract(numerator.multiply(value.denominator).add(denominator.multiply(value.numerator)), denominator.multiply(value.denominator));
    }

    public BigFract multiply(long value)
    {
        return multiply(BigInteger.valueOf(value));
    }
    public BigFract multiply(BigInteger value)
    {
        return new BigFract(numerator.multiply(value), denominator);
    }
    public BigFract multiply(BigFract value)
    {
        return new BigFract(numerator.multiply(value.numerator), denominator.multiply(value.denominator));
    }

    public int compareTo(BigFract o)
    {
        // TODO is this negative-safe?
        return numerator.multiply(o.denominator).compareTo(denominator.multiply(o.numerator));
    }

    public String toString()
    {
        return numerator + (denominator.equals(BigInteger.ONE)? "" : " / " + denominator);
    }

    public String toPositionalString(int scale)
    {
        return toPositionalString(scale, 10, false);
    }

    /**
     * Returns a string representing this fraction rounded to <code>scale</code> positional places.
     */
    public String toPositionalString(int scale, int radix, boolean scientific)
    {
        // TODO this is horribly wrong for negative rounding scales
        BigInteger scaler = BigInteger.valueOf(radix).pow(scale);
        BigInteger scaled = scaler.multiply(numerator).add(denominator.divide(TWO)).divide(denominator);
        String tmp = scaled.toString(radix);
        if (scientific)
        {
            return tmp.substring(0, 1) + (tmp.length() > 1? "." + tmp.substring(1) : "") + " × " + radix + "^" + (tmp.length() - scale - 1);
        }
        if (scale >= 1)
        {
            tmp = StringUtils.leftPad(tmp, scale + 1, "0");
            return tmp.substring(0, tmp.length() - scale) + "." + tmp.substring(tmp.length() - scale);
        }
        return scaled.multiply(scaler).toString(radix);
    }
}
