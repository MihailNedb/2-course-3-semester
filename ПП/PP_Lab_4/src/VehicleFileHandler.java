import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.logging.*;
import java.util.zip.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class VehicleFileHandler extends BaseFileHandler<Vehicle> {
    private static final Logger logger = Logger.getLogger(VehicleFileHandler.class.getName());
    private static final String SECRET_KEY = "MySecretKey12345";

    public VehicleFileHandler(String readFilename, String writeFilename) {
        super(readFilename, writeFilename);
        setupLogger();
    }

    private void setupLogger() {
        try {
            java.util.logging.FileHandler logFileHandler =
                    new java.util.logging.FileHandler("transport_errors.log", true);
            logFileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(logFileHandler);
            logger.setLevel(Level.WARNING);
        } catch (IOException e) {
            System.err.println("Ошибка настройки логгера: " + e.getMessage());
        }
    }

    @Override
    public List<Vehicle> readFromFile() {
        List<Vehicle> vehicles = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(readFilename))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    Vehicle vehicle = parseLine(line);
                    if (vehicle != null) {
                        vehicles.add(vehicle);
                    }
                } catch (Exception e) {
                    String errorMsg = "Ошибка в строке " + lineNumber + ": " + line + " - " + e.getMessage();
                    System.err.println(errorMsg);
                    logger.warning(errorMsg);
                }
            }

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }

        return vehicles;
    }

    @Override
    public void writeToFile(List<Vehicle> vehicles) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(writeFilename))) {
            for (Vehicle vehicle : vehicles) {
                writer.println(convertToString(vehicle));
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
        }
    }

    @Override
    protected Vehicle parseLine(String line) {
        String[] parts = line.split("\\s*,\\s*");
        if (parts.length != 7) {
            throw new IllegalArgumentException("Неверное количество полей. Ожидается 7x, получено: " + parts.length);
        }

        try {
            String id = parts[0].trim();
            String type = parts[1].trim();
            String model = parts[2].trim();

            double enginePower;
            try {
                String powerStr = parts[3].trim().replace(',', '.');
                enginePower = Double.parseDouble(powerStr);
                if (enginePower <= 0) throw new IllegalArgumentException("Мощность должна быть положительной");
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Некорректная мощность двигателя: " + parts[3].trim());
            }

            int maxSpeed;
            try {
                maxSpeed = Integer.parseInt(parts[4].trim());
                if (maxSpeed <= 0) throw new IllegalArgumentException("Скорость должна быть положительной");
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Некорректная максимальная скорость: " + parts[4].trim());
            }

            Date manufactureDate;
            try {
                manufactureDate = dateFormat.parse(parts[5].trim());
            } catch (ParseException e) {
                throw new IllegalArgumentException("Некорректный формат даты: " + parts[5].trim() +
                        ". Ожидается: дд.мм.гггг");
            }

            double price;
            try {
                String priceStr = parts[6].trim().replace(',', '.');
                price = Double.parseDouble(priceStr);
                if (price <= 0) throw new IllegalArgumentException("Цена должна быть положительной");
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Некорректная цена: " + parts[6].trim());
            }

            return new Vehicle(id, type, model, enginePower, maxSpeed, manufactureDate, price);

        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private String convertToString(Vehicle vehicle) {
        return String.format("%s, %s, %s, %.0f, %d, %s, %.0f",
                vehicle.getId(), vehicle.getType(), vehicle.getModel(),
                vehicle.getEnginePower(), vehicle.getMaxSpeed(),
                dateFormat.format(vehicle.getManufactureDate()),
                vehicle.getPrice());
    }


    public String encryptData(String data) throws Exception {
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decryptData(String encryptedData) throws Exception {
        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    // Архивация в ZIP
    public void createZipArchive(String sourceFile, String zipFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream(sourceFile)) {

            ZipEntry zipEntry = new ZipEntry(new File(sourceFile).getName());
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
    }


    public void extractZipArchive(String zipFile, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(destDir, zipEntry.getName());
                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                }
                zipEntry = zis.getNextEntry();
            }
        }
    }
}