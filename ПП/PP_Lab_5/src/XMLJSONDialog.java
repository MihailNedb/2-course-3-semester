import javax.swing.*;
import java.awt.*;
import java.util.List;

public class XMLJSONDialog extends JDialog {
    private XMLJSONHandler handler;
    private String mode; // "xml" или "json"

    private JTextArea resultArea;
    private JTextField filenameField;

    public XMLJSONDialog(JFrame parent, String title, String mode) {
        super(parent, title, true);
        this.mode = mode;
        this.handler = new XMLJSONHandler();

        setSize(700, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        // Панель управления
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Поле имени файла
        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(new JLabel("Имя файла:"), gbc);
        gbc.gridx = 1;
        filenameField = new JTextField(20);
        filenameField.setText(mode.equals("xml") ? "vehicles.xml" : "vehicles.json");
        controlPanel.add(filenameField, gbc);

        // Кнопки операций
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton saveButton = createOperationButton("Сохранить");
        saveButton.setForeground(Color.BLACK);
        saveButton.addActionListener(e -> saveData());

        JButton loadButton = createOperationButton("Загрузить");
        loadButton.setForeground(Color.BLACK);
        loadButton.addActionListener(e -> loadData());

        JButton viewButton = createOperationButton("Просмотреть");
        viewButton.setForeground(Color.BLACK);
        viewButton.addActionListener(e -> viewData());

        JButton convertButton = createOperationButton("Конвертировать");
        convertButton.setForeground(Color.BLACK);
        convertButton.addActionListener(e -> convertData());

        JButton closeButton = new JButton("Закрыть");
        closeButton.setForeground(Color.BLACK);
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(convertButton);
        buttonPanel.add(closeButton);

        controlPanel.add(buttonPanel, gbc);

        // Область результатов
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setForeground(Color.BLACK); // Устанавливаем черный цвет текста
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Результат"));

        // Добавление на форму
        add(controlPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JButton createOperationButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(33, 150, 243));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }

    public void saveData() {
        try {
            // Получаем данные из главного окна
            VehicleTableGUI mainFrame = (VehicleTableGUI) getOwner();
            VehicleTableModel model = mainFrame.tableModel;
            List<Vehicle> vehicles = model.getAllVehicles();

            String filename = filenameField.getText().trim();
            if (filename.isEmpty()) {
                filename = mode.equals("xml") ? "vehicles.xml" : "vehicles.json";
            }

            if (mode.equals("xml")) {
                handler.writeToXML(vehicles, filename);
                resultArea.setText("Данные успешно сохранены в XML файл: " + filename +
                        "\n\nСохранено транспортных средств: " + vehicles.size());
            } else {
                handler.writeToJSON(vehicles, filename);
                resultArea.setText("Данные успешно сохранены в JSON файл: " + filename +
                        "\n\nСохранено транспортных средств: " + vehicles.size());
            }

        } catch (Exception e) {
            resultArea.setText("Ошибка сохранения: " + e.getMessage());
        }
    }

    private void loadData() {
        try {
            String filename = filenameField.getText().trim();
            if (filename.isEmpty()) {
                filename = mode.equals("xml") ? "vehicles.xml" : "vehicles.json";
            }

            List<Vehicle> vehicles;
            if (mode.equals("xml")) {
                vehicles = handler.readFromXML(filename);
            } else {
                vehicles = handler.readFromJSON(filename);
            }

            // Обновляем данные в главном окне
            VehicleTableGUI mainFrame = (VehicleTableGUI) getOwner();
            mainFrame.tableModel.setVehicles(vehicles);

            // Выводим все транспортные средства в resultArea
            StringBuilder resultText = new StringBuilder();
            resultText.append("Данные успешно загружены из ").append(filename)
                    .append("\n\nЗагружено транспортных средств: ").append(vehicles.size()).append("\n\n");

            if (vehicles.isEmpty()) {
                resultText.append("Нет данных");
            } else {
                // Перебор всех транспортных средств и вывод их информации
                for (Vehicle vehicle : vehicles) {
                    resultText.append(vehicle).append("\n");
                }
            }

            resultArea.setText(resultText.toString());

        } catch (Exception e) {
            resultArea.setText("Ошибка загрузки: " + e.getMessage());
        }
    }

    private void viewData() {
        try {
            String filename = filenameField.getText().trim();
            if (filename.isEmpty()) {
                filename = mode.equals("xml") ? "vehicles.xml" : "vehicles.json";
            }

            java.io.File file = new java.io.File(filename);
            if (!file.exists()) {
                resultArea.setText("Файл не найден: " + filename);
                return;
            }

            StringBuilder content = new StringBuilder();
            try (java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }

            resultArea.setText("Содержимое файла " + filename + ":\n\n" + content.toString());

        } catch (Exception e) {
            resultArea.setText("Ошибка чтения файла: " + e.getMessage());
        }
    }

    private void convertData() {
        try {
            String sourceFile = filenameField.getText().trim();
            if (sourceFile.isEmpty()) {
                sourceFile = mode.equals("xml") ? "vehicles.xml" : "vehicles.json";
            }

            String targetFile;
            if (mode.equals("xml")) {
                targetFile = sourceFile.replace(".xml", ".json");
                if (targetFile.equals(sourceFile)) targetFile = "vehicles.json";
                handler.copyXMLtoJSON(sourceFile, targetFile);
            } else {
                targetFile = sourceFile.replace(".json", ".xml");
                if (targetFile.equals(sourceFile)) targetFile = "vehicles.xml";
                handler.copyJSONtoXML(sourceFile, targetFile);
            }

            resultArea.setText("Данные успешно сконвертированы!\n" +
                    "Из: " + sourceFile + "\n" +
                    "В: " + targetFile);

        } catch (Exception e) {
            resultArea.setText("Ошибка конвертации: " + e.getMessage());
        }
    }
}
