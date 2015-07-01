/**
 * CommutativeRing - a ring with commutative multiplication
 */
public interface CommutativeRing<U, T extends Commutative<U> & Monoid<U>> extends Ring<U>
{
    T multiplication();
}
