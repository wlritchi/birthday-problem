public class Zmod2pow32 implements CommutativeRing<Integer, Zmod2pow32.Multiplication>
{
    private static final Zmod2pow32 INSTANCE = new Zmod2pow32();

    private Zmod2pow32()
    {
    }

    public Zmod2pow32 getInstance()
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

    static class Addition implements AbelianGroup<Integer>
    {
        private static final Addition INSTANCE = new Addition();

        private Addition()
        {
        }

        public static Addition getInstance()
        {
            return INSTANCE;
        }

        public Integer operation(Integer point1, Integer point2)
        {
            return point1 + point2;
        }

        public Integer inverse(Integer point)
        {
            return -point;
        }

        public Integer identity()
        {
            return 0;
        }
    }

    static class Multiplication implements Commutative<Integer>, Monoid<Integer>
    {
        private static final Multiplication INSTANCE = new Multiplication();

        private Multiplication()
        {
        }

        public static Multiplication getInstance()
        {
            return INSTANCE;
        }

        public Integer operation(Integer point1, Integer point2)
        {
            return point1 * point2;
        }

        public Integer identity()
        {
            return 1;
        }
    }
}
