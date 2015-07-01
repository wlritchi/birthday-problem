import java.util.List;

public class Manhattan<U, V> implements Metric<List<U>, V>
{
    private final Metric<U, V> metric;
    private final Monoid<V> monoid;

    public Manhattan(Metric<U, V> metric, Monoid<V> monoid)
    {
        this.metric = metric;
        this.monoid = monoid;
    }

    public V distance(List<U> point1, List<U> point2)
    {
        int size = point1.size();
        if (size != point2.size())
        {
            throw new IllegalArgumentException("Cannot compute manhattan distance on vectors of different sizes");
        }
        V acc = monoid.identity();
        for (int i = 0; i < size; i++)
        {
            acc = monoid.operation(acc, metric.distance(point1.get(i), point2.get(i)));
        }
        return acc;
    }
}
