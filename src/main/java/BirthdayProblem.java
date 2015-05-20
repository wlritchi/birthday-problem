import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.RandomAccess;
import java.util.Scanner;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;

import static java.math.RoundingMode.CEILING;
import static java.math.RoundingMode.FLOOR;

public class BirthdayProblem
{
    /**
     * Partial factorials for which n-r is greater than this value will be split for processing
     */
    private static final int PFACT_SPLIT_THRESHOLD = 1000;
    private static final BigFract FIFTY_PERCENT = new BigFract(1, 2);
    private static final BigFract NINETY_PERCENT = new BigFract(9, 10);

    private final int numDays;

    private final LoadingCache<Pair<Integer, Integer>, BigInteger> partialFactorial;
    private final List<BigFract> probabilityOfMatch;
    
    public BirthdayProblem(int numDays)
    {
        this.numDays = numDays;
        partialFactorial = new NeighbourLoadingCache<Pair<Integer, Integer>, Integer, BigInteger>(new Pair.Impl<Integer, Integer>(1, 0), BigInteger.ONE, PFactMetric.getInstance(), Comparator.<Integer>naturalOrder())
            {
                private BigInteger getRecursive(int n, int r)
                {
                    try
                    {
                        return partialFactorial.get(new Pair.Impl<>(n, r));
                    }
                    catch (ExecutionException e)
                    {
                        e.printStackTrace();
                        System.exit(1);
                        return null;
                    }
                }

                public BigInteger generateFromNeighbour(Pair<Integer, Integer> neighbourKey, BigInteger neighbourValue, Pair<Integer, Integer> newKey)
                {
                    int a = newKey.first();
                    int b = newKey.second();
                    int c = neighbourKey.first();
                    int d = neighbourKey.second();

                    //System.out.println("  (" + a + "," + b + "):" + (a - b));

                    if (a - b < PFactMetric.getInstance().distance(neighbourKey, newKey))
                    {
                        // TODO this is pretty broken because stack overflow (probably because I did it wrong)
                        // So fix it by reworking the cache
                        /*if (a - b > PFACT_SPLIT_THRESHOLD)
                        {
                            int size = a - b;
                            int splits = (size + PFACT_SPLIT_THRESHOLD - 1) / PFACT_SPLIT_THRESHOLD;
                            List<BigInteger> bignums = new ArrayList<>(splits);
                            for (int i = 0; i < splits - 1; i++)
                            {
                                bignums.add(getRecursive(b + (i + 1) * PFACT_SPLIT_THRESHOLD, b + i * PFACT_SPLIT_THRESHOLD));
                            }
                            bignums.add(getRecursive(a, b + (splits - 1) * PFACT_SPLIT_THRESHOLD));
                            return listProduct(bignums, 0, splits);
                        }*/
                        return partialFactorial(a, b);
                    }

                    BigInteger acc = neighbourValue;
                    if (a > c)
                    {
                        acc = acc.multiply(partialFactorial(a, c));
                    }
                    else if (c > a)
                    {
                        acc = acc.divide(partialFactorial(c, a));
                    }
                    if (b > d)
                    {
                        acc = acc.divide(partialFactorial(b, d));
                    }
                    else if (d > b)
                    {
                        acc = acc.multiply(partialFactorial(d, b));
                    }
                    return acc;
                }
            };
        probabilityOfMatch = new LazyList<>(new CacheLoader<Integer, BigFract>()
            {
                private String printBigInt(BigInteger big)
                {
                    String s = big.toString();
                    if (s.length() < 20)
                    {
                        return s;
                    }
                    return s.substring(0, 1) + "." + s.substring(1, 10) + " × 10^" + (s.length() - 1);
                }

                public BigFract load(Integer numPeople)
                {
                    try
                    {
                        BigInteger d_nfact = partialFactorial.get(new Pair.Impl<>(numDays, numDays - numPeople));
                        BigInteger den = BigInteger.valueOf(numDays).pow(numPeople);
                        BigInteger num = den.subtract(d_nfact);
                        return new BigFract(num, den);
                    }
                    catch (ExecutionException e)
                    {
                        e.printStackTrace();
                        System.exit(1);
                        return null;
                    }
                }
            }, numDays + 1);
    }

    public static void main(String[] args)
    {
        Scanner s = new Scanner(System.in);
        System.out.print("Number of days? ");
        BirthdayProblem bp = new BirthdayProblem(s.nextInt());
        s.nextLine();
        System.out.print("Number of people? [find] ");
        String line = s.nextLine().trim();
        if (line.isEmpty())
        {
            System.out.println("50% probability of match after " + bp.peopleForProbability(FIFTY_PERCENT) + " people");
            System.out.println("90% probability of match after " + bp.peopleForProbability(NINETY_PERCENT) + " people");
        }
        else
        {
            System.out.println("Probability of match: " + bp.probabilityOfMatch.get(new Integer(line)).multiply(100).toPositionalString(1) + "%");
        }
    }

    private int peopleForProbability(BigFract needle)
    {
        int index = Collections.binarySearch(probabilityOfMatch, needle);
        if (index < 0)
        {
            index = -1 - index;
        }
        return index;
    }

    /**
     * Computes n! divided by r!; that is, the product of all numbers from r+1 to n, inclusive.
     */
    private static BigInteger partialFactorial(int n, int r)
    {
        //System.out.println("[" + n + "," + r + "]:" + (n - r));
        if (r >= n)
        {
            throw new IllegalArgumentException("r(" + r + ") must be < n(" + n + ")");
        }
        if (r < 0)
        {
            throw new IllegalArgumentException("r(" + r + ") must be >= 0");
        }

        ArrayList<BigInteger> bignums = new ArrayList<BigInteger>(IntMath.divide(n * IntMath.log2(n, CEILING), Long.SIZE, CEILING));

        int startingNumber = r + 1;
        long product = 1;
        int shift = 0;
        int productBits = 0;
        int bits = LongMath.log2(startingNumber, FLOOR) + 1;
        int nextPowerOfTwo = 1 << (bits - 1);
        for (long num = startingNumber; num <= n; num++)
        {
            if ((num & nextPowerOfTwo) != 0) {
                nextPowerOfTwo <<= 1;
                bits++;
            }
            int tz = Long.numberOfTrailingZeros(num);
            long normalizedNum = num >> tz;
            shift += tz;
            int normalizedBits = bits - tz;
            if (normalizedBits + productBits >= Long.SIZE) {
                bignums.add(BigInteger.valueOf(product));
                product = 1;
            }
            product *= normalizedNum;
            productBits = LongMath.log2(product, FLOOR) + 1;
        }
        if (product > 1)
        {
            bignums.add(BigInteger.valueOf(product));
        }
        return listProduct(bignums, 0, bignums.size()).shiftLeft(shift);
    }

    private static BigInteger listProduct(List<BigInteger> nums, int start, int end)
    {
        switch (end - start)
        {
            case 0:
                return BigInteger.ONE;
            case 1:
                return nums.get(start);
            case 2:
                return nums.get(start).multiply(nums.get(start + 1));
            case 3:
                return nums.get(start).multiply(nums.get(start + 1)).multiply(nums.get(start + 2));
            default:
                int m = (end + start) >>> 1;
                return listProduct(nums, start, m).multiply(listProduct(nums, m, end));
        }
    }

    private static class PFactMetric implements Metric<Pair<Integer, Integer>, Integer>
    {
        private static final PFactMetric INSTANCE = new PFactMetric();

        private PFactMetric()
        {
        }

        public static PFactMetric getInstance()
        {
            return INSTANCE;
        }

        public Integer distance(Pair<Integer, Integer> point1, Pair<Integer, Integer> point2)
        {
            int a = point1.first();
            int b = point1.second();
            int c = point2.first();
            int d = point2.second();

            if (b > c || d > a) // no overlap
            {
                return a - b + c - d;
            }

            return Math.abs(a - c) + Math.abs(b - d);
        }
    }
}
