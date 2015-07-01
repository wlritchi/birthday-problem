import java.math.BigInteger;

public class Z implements CommutativeRing<BigInteger, Z.Multiplication>
{
    private static final Z INSTANCE = new Z();

    private Z()
    {
    }

    public static Z getInstance()
    {
        return INSTANCE;
    }

    public Addition addition()
    {
        return Addition.getInstance();
    }

    public Multiplication multiplication()
    {
        return Multiplication.getInstance();
    }

    static class Addition implements AbelianGroup<BigInteger>
    {
        private static final Addition INSTANCE = new Addition();

        private Addition()
        {
        }

        public static Addition getInstance()
        {
            return INSTANCE;
        }

        public BigInteger operation(BigInteger point1, BigInteger point2)
        {
            return point1.add(point2);
        }

        public BigInteger inverse(BigInteger point)
        {
            return point.negate();
        }

        public BigInteger identity()
        {
            return BigInteger.ZERO;
        }
    }

    static class Multiplication implements Commutative<BigInteger>, Monoid<BigInteger>
    {
        private static final Multiplication INSTANCE = new Multiplication();

        private Multiplication()
        {
        }

        public static Multiplication getInstance()
        {
            return INSTANCE;
        }

        public BigInteger operation(BigInteger point1, BigInteger point2)
        {
            return point1.multiply(point2);
        }

        public BigInteger identity()
        {
            return BigInteger.ONE;
        }
    }
}
