import java.io.*;
import javax.swing.*;

public class TransportCompany {
    public static void main(String[] args) {
        createTestData();

        // Запрашиваем режим работы
        String[] options = {"Графический интерфейс (Swing)", "Консольное меню"};
        int choice = JOptionPane.showOptionDialog(null,
                "Выберите режим работы программы:",
                "Транспортная компания",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
            // Графический интерфейс
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new VehicleTableGUI().setVisible(true);
            });
        } else {
            // Консольное меню
            Menu menu = new Menu();
            menu.showMenu();
        }
    }

    public static void createTestData() {
        File testFile = new File("input.txt");
        if (!testFile.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter("input.txt"))) {
                writer.println("V001, суша, Toyota Camry, 180, 220, 15.05.2020, 25000");
                writer.println("V002, воздух, Boeing 737, 24000, 850, 20.10.2018, 85000000");
                writer.println("V003, вода, Yamaha 242, 360, 65, 12.03.2021, 45000");
                writer.println("V004, суша, BMW X5, 340, 240, 10.06.2022, 75000");
                writer.println("V005, воздух, Airbus A320, 26000, 870, 05.09.2019, 92000000");
                writer.println("V006, вода, Sea Ray 270, 420, 75, 22.11.2020, 68000");

                System.out.println("Создан тестовый файл input.txt с 6 транспортными средствами");
            } catch (IOException e) {
                System.err.println("Ошибка создания тестового файла: " + e.getMessage());
            }
        }

        createDecoratorTestData();
    }

    private static void createDecoratorTestData() {
        File decoratedFile = new File("decorated_demo.txt");
        if (!decoratedFile.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter("decorated_demo.txt"))) {
                writer.println("=== ДЕМОНСТРАЦИЯ DECORATOR PATTERN ===");
                writer.println();
                writer.println("Пример 1: Спорткар с улучшениями");
                writer.println("Базовое ТС: Toyota Camry (180 л.с., 220 км/ч, $25,000)");
                writer.println("Улучшения:");
                writer.println("  - Турбонаддув (+50 л.с., +$5,000)");
                writer.println("  - Спойлер из карбона (+20 км/ч, +$2,500)");
                writer.println("  - Аудиосистема Bose (+$3,000)");
                writer.println("Итог: 230 л.с., 240 км/ч, $35,500");
                writer.println();
                writer.println("Пример 2: Бронированный внедорожник");
                writer.println("Базовое ТС: BMW X5 (340 л.с., 240 км/ч, $75,000)");
                writer.println("Улучшения:");
                writer.println("  - Бронирование уровня 3 (-20 л.с., +$10,000)");
                writer.println("  - Навигация Garmin Premium (+$1,500)");
                writer.println("Итог: 320 л.с., 240 км/ч, $86,500");

                System.out.println("Создан демонстрационный файл decorated_demo.txt");
            } catch (IOException e) {
                System.err.println("Ошибка создания файла демонстрации: " + e.getMessage());
            }
        }
    }
}