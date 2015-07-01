/**
 * Rng - a set with a semigroup multiplication operator that distributes over an abelian group addition operator
 */
public interface Rng<U>
{
    AbelianGroup<U> addition();
    Semigroup<U> multiplication();
}
