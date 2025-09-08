import java.util.Scanner;
import java.util.Formatter;
import java.math.BigDecimal;
import java.math.MathContext;

public class PP_LAB_1 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("Введите число x: ");
        BigDecimal x = new BigDecimal(in.next());
        BigDecimal pi2 = new BigDecimal(6.2832);
        BigDecimal pi3 = new BigDecimal(6.2830);
while(x.compareTo(pi2) > 0)
{
x=x.subtract(pi3);
}
        System.out.print("Введите точность k: ");
        int k = in.nextInt();

        BigDecimal epsilon = BigDecimal.ONE.movePointLeft(k);
        BigDecimal result = BigCalculator.calculateSin(x, epsilon);
        BigDecimal expected = new BigDecimal(Math.sin(x.doubleValue()));

        Formatter formatter = new Formatter();

        int roundedResult = result.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        int roundedExpected = expected.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        /*
        formatter.format("Округленный результат (8): %o%n", roundedResult);
        formatter.format("Округленный результат (16): %x%n", roundedResult);
        formatter.format("Округленное ожидаемое (8): %o%n", roundedExpected);
        formatter.format("Округленное ожидаемое (16): %x%n%n", roundedExpected);
*/
        int precision = k + 1;
        int width = 15;

        formatter.format("Результат: %+" + width + "." + precision + "f%n", result);
        formatter.format("Ожидаемое: %+" + width + "." + precision + "f%n", expected);
        formatter.format("Разница:   %+" + width + "." + precision + "f%n%n", result.subtract(expected).abs());

        formatter.format("С флагом 0 и #: %0" + (width + 5) + ".3f%n", result);
        formatter.format("С флагом + и #: %+" + width + "." + precision + "f%n", result);
        formatter.format("Шестнадцатеричное с #: %#x%n", roundedResult);

        System.out.println(formatter.toString());

        System.out.println("\nВывод через System.out.printf():");
        System.out.printf("Результат: %+" + width + "." + precision + "f%n", result);
        System.out.printf("Ожидаемое: %+" + width + "." + precision + "f%n", expected);
        System.out.printf("Разница:   %0" + (width + 2) + "." + precision + "f%n", result.subtract(expected).abs());
        System.out.printf("Восьмеричное: %#o%n", roundedResult);
        System.out.printf("Шестнадцатеричное: %#x%n", roundedResult);
    }
}

class SimpleCalculator {
    public static double calculateSin(double x, int k) {
        double epsilon = Math.pow(10, -k);
        double sum = 0;
        double term = x;
        int n = 1;

        while (Math.abs(term) >= epsilon) {
            sum += term;
            term = -term * x * x / ((2 * n) * (2 * n + 1));
            n++;
        }

        return sum;
    }
}

class BigCalculator {
    public static BigDecimal calculateSin(BigDecimal x, BigDecimal epsilon) {
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal term = x;
        int n = 1;
        MathContext mc = new MathContext(50);

        while (term.abs().compareTo(epsilon) >= 0) {
            sum = sum.add(term);
            BigDecimal numerator = term.multiply(x).multiply(x).negate();
            BigDecimal denominator = new BigDecimal(2 * n).multiply(new BigDecimal(2 * n + 1));
            term = numerator.divide(denominator, mc);
            n++;
        }

        return sum;
    }
}