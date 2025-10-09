import java.io.*;
import java.util.*;
import java.text.*;
import java.util.regex.*;

public class Main {
    //aaadqdqwc 1011&10:15:30;35%%1111 00:60:00 eeacwe&11v01:25:45:90&&1010 23:59:59 g&&1001 24:00:00 weopwpvoe 1110 12:00:00&99:99:99 jkcnwe 1100 00:00:00 finish 1000 13:61:30&0110 19:30:25 woenc 1010101 08:15:00&111111 17:45:23  0001 31:12:12 fil 0011 06:30:00 fdqw
    public static void main(String[] args) {

        try {
            BufferedReader fileBr = new BufferedReader(new FileReader("input.txt"));
            String data = fileBr.readLine();
            String sep = fileBr.readLine();


            process(data, sep);

            System.out.println("Результат в 'output.txt'");

        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    private static void process(String data, String sep) throws IOException {

        String pattern = "[" + Pattern.quote(sep) + "]+";
        String[] tokens = data.split(pattern);

        List<String> binList = new ArrayList<>();
        List<String> timeList = new ArrayList<>();

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (isBin(token)) {
                binList.add(token);
            }

            if (isTime(token)) {
                timeList.add(token);
            }
        }

        String modified = modify(data, binList, sep);

        String report = createReport(data, sep, binList, timeList, modified);

        writeFile("output.txt", report);
    }

    private static boolean isBin(String s) {
        if (s == null || s.isEmpty()) return false;
        for (char c : s.toCharArray()) {
            if (c != '0' && c != '1') return false;
        }
        return true;
    }

    private static boolean isTime(String s) {
        if (!s.matches("^\\d{2}:\\d{2}:\\d{2}$")) return false;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("mm/HH/ss");
            sdf.setLenient(false);
            sdf.parse(s);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private static String modify(String orig, List<String> bins, String sep) {
        StringBuilder sb = new StringBuilder(orig);
        Random rand = new Random();
        int num = rand.nextInt(100) + 1;

        boolean added = false;

        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '0' || sb.charAt(i) == '1') {
                int start = i;
                while (i < sb.length() && (sb.charAt(i) == '0' || sb.charAt(i) == '1')) {
                    i++;
                }
                String candidate = sb.substring(start, i);

                if (isBin(candidate)) {
                    sb.insert(i, " " + num);
                    added = true;
                    break;
                }
            }
        }

        if (!added) {
            int mid = sb.length() / 2;
            sb.insert(mid, " " + num + " ");
        }

        String toRemove = findSub(sb.toString());
        if (toRemove != null) {
            int idx = sb.indexOf(toRemove);
            sb.delete(idx, idx + toRemove.length());
        }

        return sb.toString();
    }

    private static String findSub(String s) {
        for (int i = s.length() - 1; i >= 0; i--) {
            if (Character.isDigit(s.charAt(i))) {
                return s.substring(i);
            }
        }
        return null;
    }

    private static String createReport(String orig, String sep,
                                       List<String> bins, List<String> times,
                                       String modified) {

        StringBuilder rep = new StringBuilder();
        rep.append("ОТЧЕТ\n");
        rep.append("Исходная строка: ").append(orig).append("\n");
        rep.append("Разделители: ").append(sep).append("\n");

        rep.append("Бинарные числа: ");
        if (bins.isEmpty()) {
            rep.append("не найдены\n");
        } else {
            rep.append(bins).append("\n");
        }

        rep.append("Время: ");
        if (times.isEmpty()) {
            rep.append("не найдено\n");
        } else {
            rep.append(times).append("\n");
        }

        rep.append("Результат: ").append(modified).append("\n");

        return rep.toString();
    }

    private static void writeFile(String name, String content) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(name))) {
            bw.write(content);
        }
    }
}