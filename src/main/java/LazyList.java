import java.util.AbstractList;
import java.util.RandomAccess;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class LazyList<E> extends AbstractList<E> implements RandomAccess
{
    protected final LoadingCache<Integer, E> cache;
    protected final int size;

    public LazyList(Function<Integer, E> backingFunction, int size)
    {
        this(CacheLoader.from(backingFunction), size);
    }
    public LazyList(CacheLoader<Integer, E> backingLoader, int size)
    {
        cache = CacheBuilder.newBuilder().build(backingLoader);
        this.size = size;
    }

    public E get(int i)
    {
        try
        {
            return cache.get(i);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public int size()
    {
        return size;
    }
}
