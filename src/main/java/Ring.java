/**
 * Ring - a rng with multiplicative identity
 */
public interface Ring<U> extends Rng<U>
{
    Monoid<U> multiplication();
}
