public interface Pair<T, U>
{
    T first();
    U second();

    public static class Impl<T, U> implements Pair<T, U>
    {
        private final T first;
        private final U second;

        public Impl(T first, U second)
        {
            if (first == null || second == null)
            {
                throw new NullPointerException();
            }
            this.first = first;
            this.second = second;
        }

        public T first()
        {
            return first;
        }

        public U second()
        {
            return second;
        }
    }
}
