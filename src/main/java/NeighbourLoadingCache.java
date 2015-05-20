import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.AbstractLoadingCache;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;

public abstract class NeighbourLoadingCache<K, M, V> extends AbstractLoadingCache<K, V>
{
    private final Cache<K, V> underlyingCache = CacheBuilder.newBuilder().build();
    private final K rootKey;
    private final V rootValue;
    private final Metric<K, M> metric;
    private final Comparator<M> comparator;
    private final Set<K> keys = Sets.newConcurrentHashSet();

    public NeighbourLoadingCache(K rootKey, V rootValue, Metric<K, M> metric, Comparator<M> comparator)
    {
        this.rootKey = rootKey;
        this.rootValue = rootValue;
        this.metric = metric;
        this.comparator = comparator;
        keys.add(rootKey);
    }

    public V getIfPresent(Object key)
    {
        if (rootKey.equals(key))
        {
            return rootValue;
        }
        return underlyingCache.getIfPresent(key);
    }

    public V get(K key) throws ExecutionException
    {
        V ret = underlyingCache.get(key, () -> {
            Set<K> liveKeys = new HashSet<>();
            Triple<K, M, Optional<V>> nearest = keys.parallelStream()
                    .<Triple<K, M, Optional<V>>>map(other -> new Triple.Impl<K, M, Optional<V>>(other, metric.distance(key, other), Optional.ofNullable(getIfPresent(other))))
                    .filter(triple -> triple.third().isPresent())
                    .peek(triple -> liveKeys.add(triple.first()))
                    .sorted((triple1, triple2) -> comparator.compare(triple1.second(), triple2.second()))
                    .findFirst()
                    .get();
            Iterator i = keys.iterator();
            while (i.hasNext())
            {
                if (!liveKeys.contains(i.next()))
                {
                    i.remove();
                }
            }
            keys.add(key);
            return generateFromNeighbour(nearest.first(), nearest.third().get(), key);
        });
        return ret;
    }

    public abstract V generateFromNeighbour(K neighbourKey, V neighbourValue, K newKey);
}
