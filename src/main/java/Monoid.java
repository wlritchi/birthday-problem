/**
 * Monoid - a semigroup with an identity element
 */
public interface Monoid<U> extends Semigroup<U>
{
    U identity();
}
