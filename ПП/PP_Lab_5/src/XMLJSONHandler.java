// XMLJSONHandler.java
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.json.*;

public class XMLJSONHandler {
    private SimpleDateFormat dateFormat;
    private Scanner scanner;
    private String lastXmlFilename;
    private String lastJsonFilename;

    public XMLJSONHandler() {
        this.dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        this.scanner = new Scanner(System.in);
        this.lastXmlFilename = null;
        this.lastJsonFilename = null;
    }

    public void setLastXmlFilename(String filename) {
        this.lastXmlFilename = filename;
    }

    public String getLastXmlFilename() {
        return this.lastXmlFilename;
    }

    public void setLastJsonFilename(String filename) {
        this.lastJsonFilename = filename;
    }

    public String getLastJsonFilename() {
        return this.lastJsonFilename;
    }

    public void writeToXML(List<Vehicle> vehicles, String filename) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("vehicles");
            doc.appendChild(rootElement);

            for (Vehicle vehicle : vehicles) {
                Element vehicleElement = doc.createElement("vehicle");
                rootElement.appendChild(vehicleElement);

                addElement(doc, vehicleElement, "id", vehicle.getId());
                addElement(doc, vehicleElement, "type", vehicle.getType());
                addElement(doc, vehicleElement, "model", vehicle.getModel());
                addElement(doc, vehicleElement, "enginePower", String.valueOf(vehicle.getEnginePower()));
                addElement(doc, vehicleElement, "maxSpeed", String.valueOf(vehicle.getMaxSpeed()));
                addElement(doc, vehicleElement, "manufactureDate", dateFormat.format(vehicle.getManufactureDate()));
                addElement(doc, vehicleElement, "price", String.valueOf(vehicle.getPrice()));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);

        } catch (Exception e) {
            System.err.println("Ошибка записи XML: " + e.getMessage());
        }
    }

    public List<Vehicle> readFromXML(String filename) {
        List<Vehicle> vehicles = new ArrayList<>();

        try {
            File f = new File(filename);
            if (!f.exists()) {
                System.err.println("Файл не найден: " + filename);
                return vehicles;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(f);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("vehicle");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String id = getElementValue(element, "id");
                    String type = getElementValue(element, "type");
                    String model = getElementValue(element, "model");
                    double enginePower = Double.parseDouble(getElementValue(element, "enginePower"));
                    int maxSpeed = Integer.parseInt(getElementValue(element, "maxSpeed"));
                    Date manufactureDate = dateFormat.parse(getElementValue(element, "manufactureDate"));
                    double price = Double.parseDouble(getElementValue(element, "price"));

                    Vehicle vehicle = new Vehicle(id, type, model, enginePower, maxSpeed, manufactureDate, price);
                    vehicles.add(vehicle);
                }
            }

        } catch (Exception e) {
            System.err.println("Ошибка чтения XML: " + e.getMessage());
        }

        return vehicles;
    }

    public void writeToJSON(List<Vehicle> vehicles, String filename) {
        try (FileWriter file = new FileWriter(filename)) {
            JSONArray jsonArray = new JSONArray();

            for (Vehicle vehicle : vehicles) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", vehicle.getId());
                jsonObject.put("type", vehicle.getType());
                jsonObject.put("model", vehicle.getModel());
                jsonObject.put("enginePower", vehicle.getEnginePower());
                jsonObject.put("maxSpeed", vehicle.getMaxSpeed());
                jsonObject.put("manufactureDate", dateFormat.format(vehicle.getManufactureDate()));
                jsonObject.put("price", vehicle.getPrice());

                jsonArray.put(jsonObject);
            }

            file.write(jsonArray.toString(4));
            file.flush();

        } catch (Exception e) {
            System.err.println("Ошибка записи JSON: " + e.getMessage());
        }
    }

    public List<Vehicle> readFromJSON(String filename) {
        List<Vehicle> vehicles = new ArrayList<>();

        try {
            File f = new File(filename);
            if (!f.exists()) {
                System.err.println("Файл не найден: " + filename);
                return vehicles;
            }

            try (FileReader reader = new FileReader(f)) {
                StringBuilder content = new StringBuilder();
                int character;
                while ((character = reader.read()) != -1) {
                    content.append((char) character);
                }

                JSONArray jsonArray = new JSONArray(content.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String id = jsonObject.getString("id");
                    String type = jsonObject.getString("type");
                    String model = jsonObject.getString("model");
                    double enginePower = jsonObject.getDouble("enginePower");
                    int maxSpeed = jsonObject.getInt("maxSpeed");
                    Date manufactureDate = dateFormat.parse(jsonObject.getString("manufactureDate"));
                    double price = jsonObject.getDouble("price");

                    Vehicle vehicle = new Vehicle(id, type, model, enginePower, maxSpeed, manufactureDate, price);
                    vehicles.add(vehicle);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка чтения JSON: " + e.getMessage());
        }

        return vehicles;
    }

    // Ввод из консоли
    public List<Vehicle> inputFromConsole() {
        List<Vehicle> vehicles = new ArrayList<>();

        System.out.print("Сколько транспортных средств вы хотите добавить? ");
        try {
            int count = Integer.parseInt(scanner.nextLine());

            for (int i = 0; i < count; i++) {
                System.out.println("Транспортное средство #" + (i + 1));
                Vehicle vehicle = inputSingleVehicle();
                if (vehicle != null) vehicles.add(vehicle);
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректное число!");
        }

        return vehicles;
    }

    private Vehicle inputSingleVehicle() {
        try {
            System.out.print("ID: ");
            String id = scanner.nextLine();

            System.out.print("Тип (суша/вода/воздух): ");
            String type = scanner.nextLine();

            System.out.print("Модель: ");
            String model = scanner.nextLine();

            System.out.print("Мощность двигателя (л.с.): ");
            double enginePower = Double.parseDouble(scanner.nextLine());

            System.out.print("Максимальная скорость (км/ч): ");
            int maxSpeed = Integer.parseInt(scanner.nextLine());

            System.out.print("Дата выпуска (дд.мм.гггг): ");
            String dateStr = scanner.nextLine();
            Date manufactureDate = dateFormat.parse(dateStr);

            System.out.print("Цена ($): ");
            double price = Double.parseDouble(scanner.nextLine());

            if (enginePower <= 0 || maxSpeed <= 0 || price <= 0) {
                System.out.println("Ошибка: числовые значения должны быть положительными!");
                return null;
            }

            return new Vehicle(id, type, model, enginePower, maxSpeed, manufactureDate, price);

        } catch (Exception e) {
            System.out.println("Ошибка ввода данных: " + e.getMessage());
            return null;
        }
    }

    public void copyXMLtoJSON(String xmlFilename, String jsonFilename) {
        try {
            List<Vehicle> vehicles = readFromXML(xmlFilename);
            if (!vehicles.isEmpty()) {
                writeToJSON(vehicles, jsonFilename);
                System.out.println("Данные успешно скопированы из " + xmlFilename + " в " + jsonFilename);
            } else {
                System.out.println("XML файл пуст или содержит ошибки!");
            }
        } catch (Exception e) {
            System.err.println("Ошибка копирования XML в JSON: " + e.getMessage());
        }
    }

    public void copyJSONtoXML(String jsonFilename, String xmlFilename) {
        try {
            List<Vehicle> vehicles = readFromJSON(jsonFilename);
            if (!vehicles.isEmpty()) {
                writeToXML(vehicles, xmlFilename);
                System.out.println("Данные успешно скопированы из " + jsonFilename + " в " + xmlFilename);
            } else {
                System.out.println("JSON файл пуст или содержит ошибки!");
            }
        } catch (Exception e) {
            System.err.println("Ошибка копирования JSON в XML: " + e.getMessage());
        }
    }

    // Чтение и вывод "сырого" XML (ровно как в файле)
    public void displayRawXML(String filename) {
        System.out.println();
        System.out.println("Содержимое файла: " + filename);
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean empty = true;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                empty = false;
            }
            if (empty) System.out.println("(файл пуст)");
        } catch (Exception e) {
            System.err.println("Ошибка чтения XML файла: " + e.getMessage());
        }
    }

    // Чтение и вывод "сырого" JSON (ровно как в файле)
    public void displayRawJSON(String filename) {
        System.out.println();
        System.out.println("Содержимое файла: " + filename);
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean empty = true;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                empty = false;
            }
            if (empty) System.out.println("(файл пуст)");
        } catch (Exception e) {
            System.err.println("Ошибка чтения JSON файла: " + e.getMessage());
        }
    }

    // Вспомогательные методы
    private void addElement(Document doc, Element parent, String tagName, String textContent) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textContent));
        parent.appendChild(element);
    }

    private String getElementValue(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
}
