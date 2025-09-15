import java.math.BigInteger;
import java.util.Scanner;

public class AaDS_rec_2 {
    private static final BigInteger a = BigInteger.valueOf(1000000007L);

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long N = in.nextLong();
        long K = in.nextLong();
        System.out.println(sochet(N, K));
    }

    public static BigInteger sochet(long N, long K) {
        BigInteger sum = BigInteger.ONE;
        for (long i = 1; i <= K; i++) {
            BigInteger v = BigInteger.valueOf(N - i + 1);
            BigInteger d = BigInteger.valueOf(i);
            sum = sum.multiply(v).divide(d);
        }
        return sum.mod(a);
    }
}