import java.util.AbstractList;

public class TwoList<U> extends AbstractList<U> implements Pair<U, U>
{
    private final U first;
    private final U second;

    public TwoList(U first, U second)
    {
        if (first == null || second == null)
        {
            throw new NullPointerException();
        }
        this.first = first;
        this.second = second;
    }

    public U first()
    {
        return first;
    }

    public U second()
    {
        return second;
    }

    public int size()
    {
        return 2;
    }

    public U get(int i)
    {
        switch(i)
        {
            case 0:
                return first;
            case 1:
                return second;
        }
        throw new IndexOutOfBoundsException();
    }
}
