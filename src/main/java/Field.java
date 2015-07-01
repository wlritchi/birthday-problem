/**
 * Field - a commutative ring with abelian multiplication, except possibly at multiplicative identity
 */
public interface Field<U, T extends Commutative<U> & Group<U>> extends CommutativeRing<U, T>
{
}
