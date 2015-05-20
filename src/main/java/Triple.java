public interface Triple<T, U, V>
{
    T first();
    U second();
    V third();

    public static class Impl<T, U, V> implements Triple<T, U, V>
    {
        private final T first;
        private final U second;
        private final V third;

        public Impl(T first, U second, V third)
        {
            if (first == null || second == null || third == null)
            {
                throw new NullPointerException();
            }
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public T first()
        {
            return first;
        }

        public U second()
        {
            return second;
        }

        public V third()
        {
            return third;
        }
    }
}
