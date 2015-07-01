/**
 * Groupoid - a set with a closed binary operator
 */
public interface Groupoid<U>
{
    U operation(U point1, U point2);
}
