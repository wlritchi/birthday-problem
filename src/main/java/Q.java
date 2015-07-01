public class Q implements Field<BigFract, Q.Multiplication>
{
    private static final Q INSTANCE = new Q();

    private Q()
    {
    }

    public static Q getInstance()
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

    static class Addition implements AbelianGroup<BigFract>
    {
        private static final Addition INSTANCE = new Addition();

        private Addition()
        {
        }

        public static Addition getInstance()
        {
            return INSTANCE;
        }

        public BigFract operation(BigFract point1, BigFract point2)
        {
            return point1.add(point2);
        }

        public BigFract inverse(BigFract point)
        {
            return point.negate();
        }

        public BigFract identity()
        {
            return BigFract.ZERO;
        }
    }

    static class Multiplication implements Commutative<BigFract>, Group<BigFract>
    {
        private static final Multiplication INSTANCE = new Multiplication();

        private Multiplication()
        {
        }

        public static Multiplication getInstance()
        {
            return INSTANCE;
        }

        public BigFract operation(BigFract point1, BigFract point2)
        {
            return point1.multiply(point2);
        }

        public BigFract inverse(BigFract point)
        {
            return point.reciprocal();
        }

        public BigFract identity()
        {
            return BigFract.ONE;
        }
    }
}
