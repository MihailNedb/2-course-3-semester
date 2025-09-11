import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class MyTask1_JT {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        ArrayList<Long> numbers = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File("in.txt"))) {
            while (scanner.hasNext()) {
                if (scanner.hasNextLong()) {
                    long num = scanner.nextLong();
                    numbers.add(num);
                }
            }

            System.out.println("Прочитанные числа: " + numbers);

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        }

        if (numbers.size() < 2) {
            try {
                Files.write(Paths.get("out.txt"), "0".getBytes());
            } catch (IOException e) {
                System.out.println("Ошибка записи файла");
            }
            return;
        }

        BigInteger final1 = Vichecl(numbers);
        try {
            Files.write(Paths.get("out.txt"), String.valueOf(final1).getBytes());
        } catch (IOException e) {
            System.out.println("Ошибка записи файла");
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Время выполнения: " + duration + " мс");
    }

    public static BigInteger Vichecl(ArrayList<Long> arr) {
        long lng = arr.get(0);
        long Kod = arr.get(1);


        if (Kod == 0) {
            if (lng >= 0) {
                System.out.println("1");
                return BigInteger.ONE;
            } else {
                System.out.println("0");
                return BigInteger.ZERO;
            }
        }

        if (arr.size() < 2 + Kod) {
            System.out.println("0");
            return BigInteger.ZERO;
        }

        /*
        H(k,n) = (n+k-1)!/(k!*(n-1)!) n-всего клеток k-выбираем
        n = Kod + 1 дырки между зелёными полосками
        k = lng - sum - Kod +1 все клетки - зеленые - минус обязательно по 1 между зелеными
        (n+i-1)/i i = 1,k
         */

        long sum = 0;

        for (int i = 2; i < arr.size(); i++) {
            sum = sum + arr.get(i);
        }

        long k = lng - sum - Kod + 1;
        if (k < 0) {
            System.out.println("0");
            return BigInteger.ZERO;
        }

        long n = Kod + 1;
        BigInteger fsum1 = BigInteger.ONE;
        for (long i = 1; i <= k; i++) {
            BigInteger v = BigInteger.valueOf(n + i - 1);
            BigInteger d = BigInteger.valueOf(i);
            fsum1 = fsum1.multiply(v).divide(d);
        }

        System.out.println(fsum1);
        return fsum1;
    }
}