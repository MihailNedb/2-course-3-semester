import java.util.*;
import java.text.SimpleDateFormat;
import java.io.*;

public class Menu {
    private Scanner scanner;
    private VehicleListStorage listStorage;
    private VehicleMapStorage mapStorage;
    private VehicleFileHandler fileHandler;
    private XMLJSONHandler xmlJsonHandler;
    private List<VehicleComponent> decoratedVehicles;

    public Menu() {
        this.scanner = new Scanner(System.in);
        this.listStorage = new VehicleListStorage();
        this.mapStorage = new VehicleMapStorage();
        this.fileHandler = new VehicleFileHandler("input.txt", "output.txt");
        this.xmlJsonHandler = new XMLJSONHandler();
        this.decoratedVehicles = new ArrayList<>();
    }

    public void showMenu() {
        loadData();

        while (true) {
            System.out.println();
            System.out.println("МЕНЮ УПРАВЛЕНИЯ ТРАНСПОРТНОЙ КОМПАНИЕЙ");
            System.out.println("1. Показать все транспортные средства");
            System.out.println("2. Добавить новое транспортное средство");
            System.out.println("3. Редактировать транспортное средство");
            System.out.println("4. Удалить транспортное средство");
            System.out.println("5. Сортировка данных");
            System.out.println("6. Сохранить в файл");
            System.out.println("7. Загрузить из файла");
            System.out.println("8. XML операции");
            System.out.println("9. JSON операции");
            System.out.println("10. Шифрование данных");
            System.out.println("11. Архивация данных");
            System.out.println("12. Decorator - Улучшения для транспортных средств");
            System.out.println("0. Выйти из программы");
            System.out.print("Выберите пункт меню: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1: displayVehicles(); break;
                    case 2: addVehicle(); break;
                    case 3: editVehicle(); break;
                    case 4: removeVehicle(); break;
                    case 5: sortVehicles(); break;
                    case 6: saveData(); break;
                    case 7: loadData(); break;
                    case 8: xmlOperations(); break;
                    case 9: jsonOperations(); break;
                    case 10: encryptionOperations(); break;
                    case 11: archivationOperations(); break;
                    case 12: decoratorOperations(); break;
                    case 0:
                        saveData();
                        System.out.println();
                        System.out.println("ДАННЫЕ СОХРАНЕНЫ. ВЫХОД ИЗ ПРОГРАММЫ.");
                        System.out.println("ДО СВИДАНИЯ!");
                        return;
                    default:
                        System.out.println("Неверный выбор! Пожалуйста, выберите пункт от 0 до 12.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число!");
            }
        }
    }

    private void loadData() {
        try {
            List<Vehicle> vehicles = fileHandler.readFromFile();
            listStorage = new VehicleListStorage();
            mapStorage = new VehicleMapStorage();
            decoratedVehicles.clear();

            for (Vehicle vehicle : vehicles) {
                listStorage.add(vehicle);
                mapStorage.add(vehicle);
            }
            System.out.println("Данные успешно загружены из файла input.txt");
            System.out.println("Загружено транспортных средств: " + vehicles.size());
        } catch (Exception e) {
            System.out.println("Ошибка при загрузке данных: " + e.getMessage());
        }
    }

    private void saveData() {
        try {
            fileHandler.writeToFile(listStorage.getAll());
            System.out.println("Данные успешно сохранены в файл output.txt");
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    private void displayVehicles() {
        System.out.println();
        System.out.println("ОТОБРАЖЕНИЕ ТРАНСПОРТНЫХ СРЕДСТВ");
        System.out.println("1. Показать из List хранилища");
        System.out.println("2. Показать из Map хранилища");
        System.out.println("3. Показать улучшенные транспортные средства (Decorator)");
        System.out.println("4. Показать все варианты");
        System.out.print("Ваш выбор: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println();
                    System.out.println("Транспортные средства (List хранилище):");
                    listStorage.displayAll();
                    break;
                case 2:
                    System.out.println();
                    System.out.println("Транспортные средства (Map хранилище):");
                    mapStorage.displayAll();
                    break;
                case 3:
                    showDecoratedVehicles();
                    break;
                case 4:
                    System.out.println();
                    System.out.println("Транспортные средства (List хранилище):");
                    listStorage.displayAll();
                    System.out.println();
                    System.out.println("Транспортные средства (Map хранилище):");
                    mapStorage.displayAll();
                    System.out.println();
                    System.out.println("Улучшенные транспортные средства:");
                    showDecoratedVehicles();
                    break;
                default:
                    System.out.println("Неверный выбор!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите число!");
        }
    }

    private void addVehicle() {
        System.out.println();
        System.out.println("ДОБАВЛЕНИЕ НОВОГО ТРАНСПОРТНОГО СРЕДСТВА");

        try {
            System.out.print("Введите ID: ");
            String id = scanner.nextLine();

            if (listStorage.findById(id) != null) {
                System.out.println("Ошибка: транспортное средство с ID '" + id + "' уже существует!");
                return;
            }

            System.out.print("Введите тип (суша/вода/воздух): ");
            String type = scanner.nextLine();

            System.out.print("Введите модель: ");
            String model = scanner.nextLine();

            System.out.print("Введите мощность двигателя (л.с.): ");
            double enginePower = Double.parseDouble(scanner.nextLine());

            System.out.print("Введите максимальную скорость (км/ч): ");
            int maxSpeed = Integer.parseInt(scanner.nextLine());

            System.out.print("Введите дату выпуска (дд.мм.гггг): ");
            String dateStr = scanner.nextLine();
            Date manufactureDate = new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);

            System.out.print("Введите цену ($): ");
            double price = Double.parseDouble(scanner.nextLine());

            if (enginePower <= 0 || maxSpeed <= 0 || price <= 0) {
                System.out.println("Ошибка: числовые значения должны быть положительными!");
                return;
            }

            Vehicle vehicle = new Vehicle(id, type, model, enginePower, maxSpeed, manufactureDate, price);
            listStorage.add(vehicle);
            mapStorage.add(vehicle);

            System.out.println();
            System.out.println("Транспортное средство успешно добавлено!");
            System.out.println(vehicle);

        } catch (Exception e) {
            System.out.println("Ошибка ввода данных: " + e.getMessage());
        }
    }

    private void editVehicle() {
        System.out.println();
        System.out.println("РЕДАКТИРОВАНИЕ ТРАНСПОРТНОГО СРЕДСТВА");

        System.out.print("Введите ID транспортного средства для редактирования: ");
        String id = scanner.nextLine();

        Vehicle vehicle = listStorage.findById(id);
        if (vehicle == null) {
            System.out.println("Транспортное средство с ID '" + id + "' не найдено!");
            return;
        }

        System.out.println();
        System.out.println("Редактирование транспортного средства:");
        System.out.println("Текущие данные: " + vehicle);
        System.out.println();
        System.out.println("(Оставьте поле пустым, чтобы сохранить текущее значение)");

        try {
            System.out.print("Новый тип (суша/вода/воздух) [" + vehicle.getType() + "]: ");
            String type = scanner.nextLine();
            if (!type.isEmpty()) vehicle.setType(type);

            System.out.print("Новая модель [" + vehicle.getModel() + "]: ");
            String model = scanner.nextLine();
            if (!model.isEmpty()) vehicle.setModel(model);

            System.out.print("Новая мощность двигателя (л.с.) [" + vehicle.getEnginePower() + "]: ");
            String powerStr = scanner.nextLine();
            if (!powerStr.isEmpty()) {
                double enginePower = Double.parseDouble(powerStr);
                if (enginePower > 0) vehicle.setEnginePower(enginePower);
                else System.out.println("Мощность должна быть положительной!");
            }

            System.out.print("Новая максимальная скорость (км/ч) [" + vehicle.getMaxSpeed() + "]: ");
            String speedStr = scanner.nextLine();
            if (!speedStr.isEmpty()) {
                int maxSpeed = Integer.parseInt(speedStr);
                if (maxSpeed > 0) vehicle.setMaxSpeed(maxSpeed);
                else System.out.println("Скорость должна быть положительной!");
            }

            System.out.print("Новая цена ($) [" + vehicle.getPrice() + "]: ");
            String priceStr = scanner.nextLine();
            if (!priceStr.isEmpty()) {
                double price = Double.parseDouble(priceStr);
                if (price > 0) vehicle.setPrice(price);
                else System.out.println("Цена должна быть положительной!");
            }

            System.out.println();
            System.out.println("Данные успешно обновлены!");
            System.out.println("Обновлённые данные: " + vehicle);

        } catch (Exception e) {
            System.out.println("Ошибка ввода данных: " + e.getMessage());
        }
    }

    private void removeVehicle() {
        System.out.println();
        System.out.println("УДАЛЕНИЕ ТРАНСПОРТНОГО СРЕДСТВА");

        System.out.print("Введите ID транспортного средства для удаления: ");
        String id = scanner.nextLine();

        Vehicle vehicle = listStorage.findById(id);
        if (vehicle != null) {
            listStorage.remove(vehicle);
            mapStorage.remove(vehicle);

            decoratedVehicles.removeIf(v -> v.getId().equals(id));

            System.out.println();
            System.out.println("Транспортное средство с ID '" + id + "' успешно удалено!");
        } else {
            System.out.println("Транспортное средство с ID '" + id + "' не найдено!");
        }
    }

    private void sortVehicles() {
        System.out.println();
        System.out.println("СОРТИРОВКА ДАННЫХ");
        System.out.println("1. По цене (по возрастанию)");
        System.out.println("2. По максимальной скорости (по возрастанию)");
        System.out.println("3. По мощности двигателя (по возрастанию)");
        System.out.println("4. По дате выпуска (от старых к новым)");
        System.out.println("5. По типу (алфавитный порядок)");
        System.out.print("Выберите поле для сортировки: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            List<Vehicle> vehicles = new ArrayList<>(listStorage.getAll());

            switch (choice) {
                case 1:
                    Collections.sort(vehicles, (v1, v2) -> Double.compare(v1.getPrice(), v2.getPrice()));
                    System.out.println();
                    System.out.println("Отсортировано по цене (по возрастанию):");
                    break;
                case 2:
                    Collections.sort(vehicles, (v1, v2) -> Integer.compare(v1.getMaxSpeed(), v2.getMaxSpeed()));
                    System.out.println();
                    System.out.println("Отсортировано по максимальной скорости (по возрастанию):");
                    break;
                case 3:
                    Collections.sort(vehicles, (v1, v2) -> Double.compare(v1.getEnginePower(), v2.getEnginePower()));
                    System.out.println();
                    System.out.println("Отсортировано по мощности двигателя (по возрастанию):");
                    break;
                case 4:
                    Collections.sort(vehicles, (v1, v2) -> v1.getManufactureDate().compareTo(v2.getManufactureDate()));
                    System.out.println();
                    System.out.println("Отсортировано по дате выпуска (от старых к новым):");
                    break;
                case 5:
                    Collections.sort(vehicles, (v1, v2) -> v1.getType().compareTo(v2.getType()));
                    System.out.println();
                    System.out.println("Отсортировано по типу (алфавитный порядок):");
                    break;
                default:
                    System.out.println("Неверный выбор!");
                    return;
            }

            for (int i = 0; i < vehicles.size(); i++) {
                System.out.println((i + 1) + ". " + vehicles.get(i));
            }

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите число!");
        }
    }

    private void xmlOperations() {
        System.out.println();
        System.out.println("XML ОПЕРАЦИИ");
        System.out.println("1. Сохранить в XML");
        System.out.println("2. Загрузить XML");
        System.out.println("3. Ввести из консоли");
        System.out.println("4. Скопировать JSON -> XML");
        System.out.println("5. Показать XML");
        System.out.print("Выберите операцию: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    xmlJsonHandler.writeToXML(listStorage.getAll(), "vehicles.xml");
                    System.out.println("Данные успешно сохранены в vehicles.xml");
                    break;
                case 2:
                    System.out.print("Введите имя XML файла (по умолчанию vehicles.xml): ");
                    String filename = scanner.nextLine().trim();
                    if (filename.isEmpty()) filename = "vehicles.xml";
                    List<Vehicle> vehicles = xmlJsonHandler.readFromXML(filename);
                    listStorage = new VehicleListStorage();
                    mapStorage = new VehicleMapStorage();
                    for (Vehicle v : vehicles) {
                        listStorage.add(v);
                        mapStorage.add(v);
                    }
                    xmlJsonHandler.setLastXmlFilename(filename);
                    System.out.println("Данные успешно загружены из " + filename);
                    break;
                case 3:
                    List<Vehicle> inputVehicles = xmlJsonHandler.inputFromConsole();
                    if (!inputVehicles.isEmpty()) {
                        xmlJsonHandler.writeToXML(inputVehicles, "vehicles.xml");
                        for (Vehicle v : inputVehicles) {
                            listStorage.add(v);
                            mapStorage.add(v);
                        }
                        xmlJsonHandler.setLastXmlFilename("vehicles.xml");
                        System.out.println("Данные введены из консоли и сохранены в vehicles.xml");
                    }
                    break;
                case 4:
                    System.out.print("Введите имя JSON файла (по умолчанию vehicles.json): ");
                    String jfile = scanner.nextLine().trim();
                    if (jfile.isEmpty()) jfile = "vehicles.json";
                    System.out.print("Введите целевой XML файл (по умолчанию vehicles.xml): ");
                    String xfile = scanner.nextLine().trim();
                    if (xfile.isEmpty()) xfile = "vehicles.xml";
                    xmlJsonHandler.copyJSONtoXML(jfile, xfile);
                    List<Vehicle> loaded = xmlJsonHandler.readFromXML(xfile);
                    listStorage = new VehicleListStorage();
                    mapStorage = new VehicleMapStorage();
                    for (Vehicle v : loaded) {
                        listStorage.add(v);
                        mapStorage.add(v);
                    }
                    xmlJsonHandler.setLastXmlFilename(xfile);
                    System.out.println("Данные скопированы из " + jfile + " в " + xfile);
                    break;
                case 5:
                    String last = xmlJsonHandler.getLastXmlFilename();
                    if (last == null || last.trim().isEmpty()) last = "vehicles.xml";
                    xmlJsonHandler.displayRawXML(last);
                    break;
                default:
                    System.out.println("Неверный выбор!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите число!");
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении XML операции: " + e.getMessage());
        }
    }

    private void jsonOperations() {
        System.out.println();
        System.out.println("JSON ОПЕРАЦИИ");
        System.out.println("1. Сохранить данные в JSON файл");
        System.out.println("2. Загрузить данные из JSON файла");
        System.out.println("3. Ввести данные из консоли и сохранить в JSON");
        System.out.println("4. Скопировать данные из XML в JSON");
        System.out.println("5. Показать JSON");
        System.out.print("Выберите операцию: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    xmlJsonHandler.writeToJSON(listStorage.getAll(), "vehicles.json");
                    System.out.println("Данные успешно сохранены в vehicles.json");
                    break;
                case 2:
                    System.out.print("Введите имя JSON файла (по умолчанию vehicles.json): ");
                    String filename = scanner.nextLine().trim();
                    if (filename.isEmpty()) filename = "vehicles.json";
                    List<Vehicle> vehicles = xmlJsonHandler.readFromJSON(filename);
                    listStorage = new VehicleListStorage();
                    mapStorage = new VehicleMapStorage();
                    for (Vehicle v : vehicles) {
                        listStorage.add(v);
                        mapStorage.add(v);
                    }
                    xmlJsonHandler.setLastJsonFilename(filename);
                    System.out.println("Данные успешно загружены из " + filename);
                    break;
                case 3:
                    List<Vehicle> inputVehicles = xmlJsonHandler.inputFromConsole();
                    if (!inputVehicles.isEmpty()) {
                        xmlJsonHandler.writeToJSON(inputVehicles, "vehicles.json");
                        for (Vehicle v : inputVehicles) {
                            listStorage.add(v);
                            mapStorage.add(v);
                        }
                        xmlJsonHandler.setLastJsonFilename("vehicles.json");
                        System.out.println("Данные введены из консоли и сохранены в vehicles.json");
                    }
                    break;
                case 4:
                    System.out.print("Введите имя XML файла (по умолчанию vehicles.xml): ");
                    String xfile = scanner.nextLine().trim();
                    if (xfile.isEmpty()) xfile = "vehicles.xml";
                    System.out.print("Введите целевой JSON файл (по умолчанию vehicles.json): ");
                    String jfile = scanner.nextLine().trim();
                    if (jfile.isEmpty()) jfile = "vehicles.json";
                    xmlJsonHandler.copyXMLtoJSON(xfile, jfile);
                    List<Vehicle> loaded = xmlJsonHandler.readFromJSON(jfile);
                    listStorage = new VehicleListStorage();
                    mapStorage = new VehicleMapStorage();
                    for (Vehicle v : loaded) {
                        listStorage.add(v);
                        mapStorage.add(v);
                    }
                    xmlJsonHandler.setLastJsonFilename(jfile);
                    System.out.println("Данные скопированы из " + xfile + " в " + jfile);
                    break;
                case 5:
                    String lastJson = xmlJsonHandler.getLastJsonFilename();
                    if (lastJson == null || lastJson.trim().isEmpty()) lastJson = "vehicles.json";
                    xmlJsonHandler.displayRawJSON(lastJson);
                    break;
                default:
                    System.out.println("Неверный выбор!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите число!");
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении JSON операции: " + e.getMessage());
        }
    }

    private void encryptionOperations() {
        System.out.println();
        System.out.println("ШИФРОВАНИЕ ДАННЫХ");
        System.out.println("1. Зашифровать данные из input.txt");
        System.out.println("2. Расшифровать данные в output.txt");
        System.out.print("Выберите операцию: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1: {
                    StringBuilder content = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            content.append(line).append("\n");
                        }
                    }

                    if (content.length() == 0) {
                        System.out.println("Файл input.txt пуст!");
                        return;
                    }

                    String encrypted = fileHandler.encryptData(content.toString());
                    try (PrintWriter writer = new PrintWriter("encrypted.txt")) {
                        writer.println(encrypted);
                    }
                    System.out.println("Данные из input.txt зашифрованы и сохранены в encrypted.txt");
                    System.out.println("Зашифрованный текст (начало): " +
                            encrypted.substring(0, Math.min(50, encrypted.length())) + "...");
                    break;
                }
                case 2: {
                    StringBuilder encryptedContent = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new FileReader("encrypted.txt"))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            encryptedContent.append(line);
                        }
                    }

                    if (encryptedContent.length() == 0) {
                        System.out.println("Файл encrypted.txt пуст!");
                        return;
                    }

                    String decrypted = fileHandler.decryptData(encryptedContent.toString());
                    try (PrintWriter writer = new PrintWriter("output.txt")) {
                        writer.print(decrypted);
                    }
                    System.out.println("Данные расшифрованы и сохранены в output.txt");
                    System.out.println("Расшифрованный текст (начало): " +
                            decrypted.substring(0, Math.min(100, decrypted.length())) + "...");
                    break;
                }
                default:
                    System.out.println("Неверный выбор!");
            }
        } catch (Exception e) {
            System.out.println("Ошибка шифрования: " + e.getMessage());
        }
    }

    private void archivationOperations() {
        System.out.println();
        System.out.println("АРХИВАЦИЯ ДАННЫХ");
        System.out.println("1. Создать ZIP архив");
        System.out.println("2. Распаковать ZIP архив");
        System.out.print("Выберите операцию: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    System.out.print("Введите имя файла для архивации (по умолчанию output.txt): ");
                    String sourceFile = scanner.nextLine().trim();
                    if (sourceFile.isEmpty()) sourceFile = "output.txt";

                    System.out.print("Введите имя ZIP архива (по умолчанию archive.zip): ");
                    String zipFile = scanner.nextLine().trim();
                    if (zipFile.isEmpty()) zipFile = "archive.zip";

                    fileHandler.createZipArchive(sourceFile, zipFile);
                    System.out.println("Архив успешно создан: " + zipFile);
                    break;
                case 2:
                    System.out.print("Введите имя ZIP архива (по умолчанию archive.zip): ");
                    String zipFileName = scanner.nextLine().trim();
                    if (zipFileName.isEmpty()) zipFileName = "archive.zip";

                    System.out.print("Введите папку для распаковки (по умолчанию extracted): ");
                    String destDirectory = scanner.nextLine().trim();
                    if (destDirectory.isEmpty()) destDirectory = "extracted";

                    fileHandler.extractZipArchive(zipFileName, destDirectory);
                    System.out.println("Архив успешно распакован в папку: " + destDirectory);
                    break;
                default:
                    System.out.println("Неверный выбор!");
            }
        } catch (Exception e) {
            System.out.println("Ошибка архивации: " + e.getMessage());
        }
    }

    private void decoratorOperations() {
        System.out.println();
        System.out.println("улучшения для транспортных средств");
        System.out.println("1. Показать все улучшенные транспортные средства");
        System.out.println("2. Добавить улучшение к существующему транспортному средству");
        System.out.println("3. Показать разницу между базовым и улучшенным транспортным средством");
        System.out.println("4. Сохранить улучшенные транспортные средства в файл");
        System.out.println("5. Создать демонстрационные улучшения");
        System.out.println("6. Информация");
        System.out.print("Выберите операцию: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1: showDecoratedVehicles(); break;
                case 2: addDecorationToVehicle(); break;
                case 3: compareVehicleDecorations(); break;
                case 4: saveDecoratedVehicles(); break;
                case 5: createDemoDecorations(); break;
                case 6: showDecoratorInfo(); break;
                default:
                    System.out.println("Неверный выбор!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите число!");
        }
    }

    private void showDecoratedVehicles() {
        System.out.println();
        System.out.println("УЛУЧШЕННЫЕ ТРАНСПОРТНЫЕ СРЕДСТВА");

        if (decoratedVehicles.isEmpty()) {
            System.out.println("Нет улучшенных транспортных средств.");
            System.out.println("Используйте пункт 2 для добавления улучшений или пункт 5 для демо.");
            return;
        }

        System.out.println("Всего улучшенных ТС: " + decoratedVehicles.size());
        System.out.println();

        for (int i = 0; i < decoratedVehicles.size(); i++) {
            VehicleComponent v = decoratedVehicles.get(i);
            System.out.println("УЛУЧШЕННОЕ ТС #" + (i + 1));
            System.out.println(v.getDescription());
            System.out.println("Итоговая цена: " + v.getPrice() + " $");
            System.out.println("Итоговая мощность: " + v.getEnginePower() + " л.с.");
            System.out.println("Итоговая макс. скорость: " + v.getMaxSpeed() + " км/ч");
            System.out.println();
        }
    }

    private void addDecorationToVehicle() {
        System.out.println();
        System.out.println("ДОБАВЛЕНИЕ УЛУЧШЕНИЙ К ТС");

        List<Vehicle> allVehicles = listStorage.getAll();
        if (allVehicles.isEmpty()) {
            System.out.println("Нет доступных транспортных средств для улучшения.");
            return;
        }

        System.out.println("Доступные транспортные средства:");
        for (int i = 0; i < allVehicles.size(); i++) {
            System.out.println((i + 1) + ". " + allVehicles.get(i));
        }

        System.out.print("Выберите номер транспортного средства: ");
        try {
            int vehicleIndex = Integer.parseInt(scanner.nextLine()) - 1;
            if (vehicleIndex < 0 || vehicleIndex >= allVehicles.size()) {
                System.out.println("Неверный номер!");
                return;
            }

            Vehicle selectedVehicle = allVehicles.get(vehicleIndex);
            VehicleComponent decoratedVehicle = selectedVehicle;

            System.out.println();
            System.out.println("Выбрано ТС: " + selectedVehicle);

            boolean continueDecorating = true;
            while (continueDecorating) {
                System.out.println();
                System.out.println("Текущее состояние:");
                System.out.println(decoratedVehicle.getDescription());
                System.out.println("Цена: " + decoratedVehicle.getPrice() + " $ | Мощность: " + decoratedVehicle.getEnginePower() + " л.с. | Скорость: " + decoratedVehicle.getMaxSpeed() + " км/ч");
                System.out.println();

                System.out.println("Выберите улучшение:");
                System.out.println("1. Покрытие (+50 л.с., +5000 $)");
                System.out.println("2. Навигационная система (+1500 $)");
                System.out.println("3. Видеоплейер (+3000 $)");
                System.out.println("4. Спойлер (+20 км/ч, +2500 $)");
                System.out.println("5. Бронирование (-20 л.с., +10000 $)");
                System.out.println("6. Завершить улучшения");
                System.out.print("Ваш выбор: ");

                int decorationChoice = Integer.parseInt(scanner.nextLine());

                switch (decorationChoice) {
                    case 1:
                        decoratedVehicle = new TurboChargerDecorator(decoratedVehicle, 50, 5000);
                        System.out.println("Добавлено покрытие!");
                        break;
                    case 2:
                        System.out.print("Введите название навигационной системы: ");
                        String navSystem = scanner.nextLine();
                        decoratedVehicle = new NavigationSystemDecorator(decoratedVehicle, navSystem, 1500);
                        System.out.println("Добавлена навигационная система!");
                        break;
                    case 3:
                        System.out.print("Введите бренд видеосистемы: ");
                        String audioBrand = scanner.nextLine();
                        decoratedVehicle = new PremiumSoundSystemDecorator(decoratedVehicle, audioBrand, 3000);
                        System.out.println("Добавлена премиальная система!");
                        break;
                    case 4:
                        System.out.print("Введите материал спойлера: ");
                        String material = scanner.nextLine();
                        decoratedVehicle = new SpoilerDecorator(decoratedVehicle, material, 2500, 20);
                        System.out.println("Добавлен спойлер!");
                        break;
                    case 5:
                        System.out.print("Введите уровень бронирования: ");
                        String armorLevel = scanner.nextLine();
                        decoratedVehicle = new ArmorDecorator(decoratedVehicle, armorLevel, 10000, 20);
                        System.out.println("Добавлено бронирование!");
                        break;
                    case 6:
                        continueDecorating = false;
                        System.out.println("Улучшения завершены!");
                        break;
                    default:
                        System.out.println("Неверный выбор!");
                }
            }

            decoratedVehicles.add(decoratedVehicle);
            System.out.println();
            System.out.println("ТРАНСПОРТНОЕ СРЕДСТВО УСПЕШНО УЛУЧШЕНО!");
            System.out.println();
            System.out.println("Итоговая цена: " + decoratedVehicle.getPrice() + " $");
            System.out.println("Итоговая мощность: " + decoratedVehicle.getEnginePower() + " л.с.");
            System.out.println("Итоговая скорость: " + decoratedVehicle.getMaxSpeed() + " км/ч");

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void compareVehicleDecorations() {
        System.out.println();
        System.out.println("СРАВНЕНИЕ БАЗОВОГО И УЛУЧШЕННОГО ТС");

        if (decoratedVehicles.isEmpty()) {
            System.out.println("Нет улучшенных транспортных средств для сравнения.");
            return;
        }

        for (int i = 0; i < decoratedVehicles.size(); i++) {
            VehicleComponent decorated = decoratedVehicles.get(i);
            Vehicle original = listStorage.findById(decorated.getId());

            if (original != null) {
                System.out.println();
                System.out.println("СРАВНЕНИЕ ТС " + original.getId());

                System.out.println();
                System.out.println("БАЗОВАЯ ВЕРСИЯ:");
                System.out.println(original.getDescription());
                System.out.println("Цена: " + original.getPrice() + " $");
                System.out.println("Мощность: " + original.getEnginePower() + " л.с.");
                System.out.println("Макс. скорость: " + original.getMaxSpeed() + " км/ч");

                System.out.println();
                System.out.println("УЛУЧШЕННАЯ ВЕРСИЯ:");
                System.out.println(decorated.getDescription());
                System.out.println("Цена: " + decorated.getPrice() + " $ (+" + (decorated.getPrice() - original.getPrice()) + " $)");
                System.out.println("Мощность: " + decorated.getEnginePower() + " л.с. (" + (decorated.getEnginePower() > original.getEnginePower() ? "+" : "") + (decorated.getEnginePower() - original.getEnginePower()) + " л.с.)");
                System.out.println("Макс. скорость: " + decorated.getMaxSpeed() + " км/ч (" + (decorated.getMaxSpeed() > original.getMaxSpeed() ? "+" : "") + (decorated.getMaxSpeed() - original.getMaxSpeed()) + " км/ч)");

                double priceIncreasePercent = ((decorated.getPrice() - original.getPrice()) / original.getPrice()) * 100;
                System.out.println();
                System.out.println("Увеличение цены: " + String.format("%.1f", priceIncreasePercent) + "%");
            }
        }
    }

    private void saveDecoratedVehicles() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("decorated_vehicles.txt"))) {
            writer.println("УЛУЧШЕННЫЕ ТРАНСПОРТНЫЕ СРЕДСТВА");
            writer.println("(Decorator Pattern Demo)");
            writer.println();
            writer.println("Дата создания: " + new Date());
            writer.println("Всего улучшенных ТС: " + decoratedVehicles.size());
            writer.println();

            for (int i = 0; i < decoratedVehicles.size(); i++) {
                VehicleComponent vehicle = decoratedVehicles.get(i);
                Vehicle original = listStorage.findById(vehicle.getId());

                writer.println("УЛУЧШЕННОЕ транспортное средство #" + (i + 1));
                writer.println("Исходное транспортное средство: " + (original != null ? original : "Не найдено"));
                writer.println("Улучшенное транспортное средство: " + vehicle.getDescription());
                writer.println("Итоговая цена: " + vehicle.getPrice() + " $");
                writer.println("Итоговая мощность: " + vehicle.getEnginePower() + " л.с.");
                writer.println("Итоговая макс. скорость: " + vehicle.getMaxSpeed() + " км/ч");

                if (original != null) {
                    writer.println("Изменения: +" + (vehicle.getPrice() - original.getPrice()) + " $, " +
                            (vehicle.getEnginePower() > original.getEnginePower() ? "+" : "") +
                            (vehicle.getEnginePower() - original.getEnginePower()) + " л.с., " +
                            (vehicle.getMaxSpeed() > original.getMaxSpeed() ? "+" : "") +
                            (vehicle.getMaxSpeed() - original.getMaxSpeed()) + " км/ч");
                }
                writer.println();
            }

            writer.println("Конец отчета");

            System.out.println("Улучшенные транспортные средства сохранены в файл decorated_vehicles.txt");
            System.out.println("Файл содержит " + decoratedVehicles.size() + " записей");

        } catch (IOException e) {
            System.out.println("Ошибка при сохранении: " + e.getMessage());
        }
    }

    private void createDemoDecorations() {
        System.out.println();
        System.out.println("СОЗДАНИЕ ДЕМОНСТРАЦИОННЫХ УЛУЧШЕНИЙ");

        List<Vehicle> allVehicles = listStorage.getAll();
        if (allVehicles.size() >= 3) {
            System.out.println("Создание демонстрационных улучшений...");

            decoratedVehicles.removeIf(v -> v.getId().startsWith("DEMO_"));

            if (allVehicles.size() > 0) {
                Vehicle sportCar = allVehicles.get(0);
                VehicleComponent decoratedSportCar = sportCar;
                decoratedSportCar = new TurboChargerDecorator(decoratedSportCar, 75, 7500);
                decoratedSportCar = new SpoilerDecorator(decoratedSportCar, "Карбон", 3500, 25);
                decoratedSportCar = new PremiumSoundSystemDecorator(decoratedSportCar, "Bose", 2800);
                decoratedSportCar = new NavigationSystemDecorator(decoratedSportCar, "GPS Pro", 1200);
                decoratedVehicles.add(decoratedSportCar);
                System.out.println("Создан спорткар с 4 улучшениями");
            }

            if (allVehicles.size() > 1) {
                Vehicle suv = allVehicles.get(1);
                VehicleComponent decoratedSUV = suv;
                decoratedSUV = new ArmorDecorator(decoratedSUV, "Уровень 3", 15000, 30);
                decoratedSUV = new NavigationSystemDecorator(decoratedSUV, "Garmin Premium", 2200);
                decoratedSUV = new PremiumSoundSystemDecorator(decoratedSUV, "JBL", 1800);
                decoratedVehicles.add(decoratedSUV);
                System.out.println("Создан бронированный внедорожник с 3 улучшениями");
            }

            if (allVehicles.size() > 2) {
                Vehicle boat = allVehicles.get(2);
                VehicleComponent decoratedBoat = boat;
                decoratedBoat = new TurboChargerDecorator(decoratedBoat, 40, 6000);
                decoratedBoat = new NavigationSystemDecorator(decoratedBoat, "Marine GPS", 1800);
                decoratedBoat = new PremiumSoundSystemDecorator(decoratedBoat, "Marine Audio", 2200);
                decoratedVehicles.add(decoratedBoat);
                System.out.println("Создана лодка с 3 улучшениями");
            }

            System.out.println();
            System.out.println("Создано " + decoratedVehicles.size() + " демонстрационных улучшений!");
            System.out.println("Используйте пункт 1 для просмотра или пункт 3 для сравнения.");

        } else {
            System.out.println("Недостаточно транспортных средств для демонстрации.");
            System.out.println("Требуется минимум 3 ТС, доступно: " + allVehicles.size());
        }
    }

    private void showDecoratorInfo() {
        System.out.println();
        System.out.println("ИНФОРМАЦИЯ О DECORATOR PATTERN");

        System.out.println();
        System.out.println("Что такое Decorator Pattern?");
        System.out.println("Decorator (Декоратор) - структурный паттерн проектирования,");
        System.out.println("который позволяет динамически добавлять объектам новую функциональность,");
        System.out.println("оборачивая их в полезные «обёртки».");

        System.out.println();
        System.out.println("Преимущества:");
        System.out.println("• Гибкость: можно добавлять любое количество улучшений");
        System.out.println("• Открытость/закрытость: можно добавлять новые типы улучшений");
        System.out.println("• Динамическое добавление: улучшения можно добавлять во время выполнения");
        System.out.println("• Прозрачность: улучшенное ТС можно использовать там же, где и обычное");

        System.out.println();
        System.out.println("Архитектура в этой программе:");
        System.out.println("1. VehicleComponent - интерфейс для всех транспортных средств");
        System.out.println("2. Vehicle - базовая реализация транспортного средства");
        System.out.println("3. VehicleDecorator - абстрактный декоратор");
        System.out.println("4. Конкретные декораторы:");
        System.out.println("   • TurboChargerDecorator - увеличивает мощность");
        System.out.println("   • NavigationSystemDecorator - добавляет навигацию");
        System.out.println("   • PremiumSoundSystemDecorator - улучшает аудиосистему");
        System.out.println("   • SpoilerDecorator - увеличивает скорость");
        System.out.println("   • ArmorDecorator - добавляет броню");

        System.out.println();
        System.out.println("Пример использования:");
        System.out.println("Vehicle car = new Vehicle(...);");
        System.out.println("car = new TurboChargerDecorator(car, 50, 5000);");
        System.out.println("car = new SpoilerDecorator(car, \"Карбон\", 2500, 20);");
        System.out.println("// Теперь car имеет все улучшения!");

        System.out.println();
        System.out.println("Файлы с примерами:");
        System.out.println("• decorated_demo.txt - примеры улучшений");
        System.out.println("• decorated_vehicles.txt - сохраненные улучшенные ТС");

        System.out.println();
        System.out.println("Попробуйте:");
        System.out.println("1. Создать демо (пункт 5)");
        System.out.println("2. Добавить улучшения к существующему транспортному средству (пункт 2)");
        System.out.println("3. Сравнить базовую и улучшенную версии (пункт 3)");
    }
}