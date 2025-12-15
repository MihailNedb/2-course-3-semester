import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VehicleDialog extends JDialog {
    private JTextField idField, typeField, modelField;
    private JTextField powerField, speedField, priceField;
    private JTextField dateField;
    private JButton okButton, cancelButton;
    private boolean confirmed = false;
    private Vehicle vehicle;

    public VehicleDialog(JFrame parent, String title, Vehicle existingVehicle) {
        super(parent, title, true);
        this.vehicle = existingVehicle;

        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initComponents();
        if (existingVehicle != null) {
            populateFields(existingVehicle);
        }
    }

    private void initComponents() {
        // Панель полей ввода
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Поля ввода
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(20);
        idField.setForeground(Color.BLACK); // Черный цвет текста
        inputPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Тип (суша/вода/воздух):"), gbc);
        gbc.gridx = 1;
        typeField = new JTextField(20);
        typeField.setForeground(Color.BLACK); // Черный цвет текста
        inputPanel.add(typeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Модель:"), gbc);
        gbc.gridx = 1;
        modelField = new JTextField(20);
        modelField.setForeground(Color.BLACK); // Черный цвет текста
        inputPanel.add(modelField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Мощность (л.с.):"), gbc);
        gbc.gridx = 1;
        powerField = new JTextField(20);
        powerField.setForeground(Color.BLACK); // Черный цвет текста
        inputPanel.add(powerField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(new JLabel("Макс. скорость (км/ч):"), gbc);
        gbc.gridx = 1;
        speedField = new JTextField(20);
        speedField.setForeground(Color.BLACK); // Черный цвет текста
        inputPanel.add(speedField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        inputPanel.add(new JLabel("Дата выпуска (дд.мм.гггг):"), gbc);
        gbc.gridx = 1;
        dateField = new JTextField(20);
        dateField.setForeground(Color.BLACK); // Черный цвет текста
        inputPanel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        inputPanel.add(new JLabel("Цена ($):"), gbc);
        gbc.gridx = 1;
        priceField = new JTextField(20);
        priceField.setForeground(Color.BLACK); // Черный цвет текста
        inputPanel.add(priceField, gbc);

        // Панель кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        okButton = new JButton("OK");
        okButton.setBackground(new Color(76, 175, 80));
        okButton.setForeground(Color.BLACK);
        okButton.addActionListener(e -> onOk());

        cancelButton = new JButton("Отмена");
        cancelButton.setBackground(new Color(244, 67, 54));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.addActionListener(e -> onCancel());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Добавление на форму
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Нажатие Enter на любом поле
        JTextField[] fields = {idField, typeField, modelField, powerField, speedField, dateField, priceField};
        for (JTextField field : fields) {
            field.addActionListener(e -> onOk());
        }
    }

    private void populateFields(Vehicle vehicle) {
        idField.setText(vehicle.getId());
        typeField.setText(vehicle.getType());
        modelField.setText(vehicle.getModel());
        powerField.setText(String.valueOf(vehicle.getEnginePower()));
        speedField.setText(String.valueOf(vehicle.getMaxSpeed()));
        dateField.setText(new SimpleDateFormat("dd.MM.yyyy").format(vehicle.getManufactureDate()));
        priceField.setText(String.valueOf(vehicle.getPrice()));
    }

    private void onOk() {
        try {
            // Валидация
            if (idField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("ID не может быть пустым");
            }

            String id = idField.getText().trim();
            String type = typeField.getText().trim();
            String model = modelField.getText().trim();

            if (type.isEmpty() || model.isEmpty()) {
                throw new IllegalArgumentException("Все поля должны быть заполнены");
            }

            double enginePower = Double.parseDouble(powerField.getText().trim());
            int maxSpeed = Integer.parseInt(speedField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());

            if (enginePower <= 0 || maxSpeed <= 0 || price <= 0) {
                throw new IllegalArgumentException("Числовые значения должны быть положительными");
            }

            Date manufactureDate = new SimpleDateFormat("dd.MM.yyyy").parse(dateField.getText().trim());

            vehicle = new Vehicle(id, type, model, enginePower, maxSpeed, manufactureDate, price);
            confirmed = true;
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка ввода: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        confirmed = false;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
