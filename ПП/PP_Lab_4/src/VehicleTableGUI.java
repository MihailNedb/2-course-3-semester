import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class VehicleTableGUI extends JFrame {
    public VehicleTableModel tableModel;
    private JTable vehicleTable;
    private VehicleFileHandler fileHandler;
    private XMLJSONHandler xmlJsonHandler;
    private List<VehicleComponent> decoratedVehicles;

    private JLabel statusLabel;
    private JLabel countLabel;

    private JButton addButton, editButton, deleteButton, decorateButton, compareButton;
    private JButton loadButton, saveButton, xmlButton, jsonButton;

    public VehicleTableGUI() {
        setTitle("Управление транспортной компанией");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        tableModel = new VehicleTableModel();
        fileHandler = new VehicleFileHandler("input.txt", "output.txt");
        xmlJsonHandler = new XMLJSONHandler();
        decoratedVehicles = new ArrayList<>();

        initUI();
        loadData();
        setupButtonActions();

        vehicleTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editVehicle();
                }
            }
        });
    }
    private void showMessage(String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, "Информация", messageType);
    }
    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // ===== TABLE =====
        vehicleTable = new JTable(tableModel);
        vehicleTable.setRowHeight(30);
        vehicleTable.setFont(new Font("Arial", Font.PLAIN, 14));
        vehicleTable.setForeground(Color.BLACK);

        // ---------- TableRowSorter с правильными Comparator ----------
        TableRowSorter<VehicleTableModel> sorter = new TableRowSorter<>(tableModel);

        // Мощность (л.с.) - столбец 3
        sorter.setComparator(3, Comparator.comparingDouble(o -> {
            if (o instanceof Number) return ((Number) o).doubleValue();
            try { return Double.parseDouble(o.toString()); } catch (Exception e) { return 0; }
        }));

        // Максимальная скорость (км/ч) - столбец 4
        sorter.setComparator(4, Comparator.comparingInt(o -> {
            if (o instanceof Number) return ((Number) o).intValue();
            try { return Integer.parseInt(o.toString()); } catch (Exception e) { return 0; }
        }));

        // Цена ($) - столбец 6
        sorter.setComparator(6, Comparator.comparingDouble(o -> {
            if (o instanceof Number) return ((Number) o).doubleValue();
            try { return Double.parseDouble(o.toString()); } catch (Exception e) { return 0; }
        }));

        // Дата выпуска - столбец 5
        sorter.setComparator(5, Comparator.comparing(o -> {
            if (o instanceof Date) return (Date) o;
            try {
                return new java.text.SimpleDateFormat("dd.MM.yyyy").parse(o.toString());
            } catch (Exception e) {
                return new Date(0);
            }
        }));

        vehicleTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(vehicleTable);

        // ---------- BUTTON PANEL ----------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(240, 240, 240));

        addButton = createButton("Добавить", new Color(76, 175, 80));
        editButton = createButton("Редактировать", new Color(33, 150, 243));
        deleteButton = createButton("Удалить", new Color(244, 67, 54));
        decorateButton = createButton("Улучшить", new Color(156, 39, 176));
        compareButton = createButton("Сравнить", new Color(255, 152, 0));

        loadButton = createButton("Загрузить", new Color(121, 85, 72));
        saveButton = createButton("Сохранить", new Color(0, 150, 136));
        xmlButton = createButton("XML", new Color(96, 125, 139));
        jsonButton = createButton("JSON", new Color(96, 125, 139));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(decorateButton);
        buttonPanel.add(compareButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(xmlButton);
        buttonPanel.add(jsonButton);

        // ---------- STATUS BAR ----------
        statusLabel = new JLabel("Готово");
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        countLabel = new JLabel("Всего ТС: 0 | Улучшенных: 0");
        countLabel.setFont(new Font("Arial", Font.BOLD, 12));
        countLabel.setForeground(Color.BLACK);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(250, 250, 250));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        infoPanel.add(countLabel, BorderLayout.WEST);
        infoPanel.add(statusLabel, BorderLayout.EAST);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFont(new Font("Arial", Font.BOLD, 12));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(color);
            }
        });
        return button;
    }

    private void setupButtonActions() {
        addButton.addActionListener(e -> addVehicle());
        editButton.addActionListener(e -> editVehicle());
        deleteButton.addActionListener(e -> deleteVehicle());
        decorateButton.addActionListener(e -> decorateVehicle());
        compareButton.addActionListener(e -> compareVehicle());
        loadButton.addActionListener(e -> loadData());
        saveButton.addActionListener(e -> saveData());
        xmlButton.addActionListener(e -> showXMLDialog());
        jsonButton.addActionListener(e -> showJSONDialog());
    }

    private void loadData() {
        try {
            List<Vehicle> vehicles = fileHandler.readFromFile();
            tableModel.setVehicles(vehicles);
            updateStatus();
            statusLabel.setText("Данные загружены: " + vehicles.size() + " ТС");
            showMessage("Данные загружены: " + vehicles.size() + " транспортных средств",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            statusLabel.setText("Ошибка загрузки");
            showMessage("Ошибка загрузки: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveData() {
        try {
            fileHandler.writeToFile(tableModel.getAllVehicles());
            updateStatus();
            statusLabel.setText("Данные сохранены");
            showMessage("Данные сохранены успешно!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            statusLabel.setText("Ошибка сохранения");
            showMessage("Ошибка сохранения: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addVehicle() {
        VehicleDialog dialog = new VehicleDialog(this, "Добавить транспортное средство", null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Vehicle vehicle = dialog.getVehicle();
            tableModel.addVehicle(vehicle);
            updateStatus();
            statusLabel.setText("Добавлено: " + vehicle.getModel());
        }
    }

    private void editVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) return;
        selectedRow = vehicleTable.convertRowIndexToModel(selectedRow);

        Vehicle vehicle = tableModel.getVehicleAt(selectedRow);
        VehicleDialog dialog = new VehicleDialog(this, "Редактировать транспортное средство", vehicle);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Vehicle updatedVehicle = dialog.getVehicle();
            tableModel.updateVehicle(selectedRow, updatedVehicle);
            updateStatus();
            statusLabel.setText("ТС обновлено");
        }
    }

    private void deleteVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) return;
        selectedRow = vehicleTable.convertRowIndexToModel(selectedRow);

        Vehicle vehicle = tableModel.getVehicleAt(selectedRow);
        tableModel.removeVehicle(selectedRow);
        tableModel.removeDecoratedVehicle(vehicle.getId());
        decoratedVehicles.removeIf(v -> v.getId().equals(vehicle.getId()));
        updateStatus();
        statusLabel.setText("ТС удалено");
    }

    private void decorateVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) return;
        selectedRow = vehicleTable.convertRowIndexToModel(selectedRow);

        Vehicle vehicle = tableModel.getVehicleAt(selectedRow);
        VehicleDecoratorDialog dialog = new VehicleDecoratorDialog(this, vehicle);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            VehicleComponent decorated = dialog.getDecoratedVehicle();
            tableModel.addDecoratedVehicle(decorated);
            decoratedVehicles.add(decorated);
            updateStatus();
            statusLabel.setText("Улучшения применены");
        }
    }

    private void compareVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) return;
        selectedRow = vehicleTable.convertRowIndexToModel(selectedRow);

        Vehicle vehicle = tableModel.getVehicleAt(selectedRow);
        VehicleComponent decorated = tableModel.getDecoratedVehicle(vehicle.getId());
        if (decorated == null) return;

        String message = "<html><body style='width: 400px;'>" +
                "<h3>Сравнение ТС " + vehicle.getId() + "</h3>" +
                "<table border='1' cellpadding='5'>" +
                "<tr><td>Модель</td><td>" + vehicle.getModel() + "</td><td>" + decorated.getModel() + "</td></tr>" +
                "<tr><td>Мощность</td><td>" + vehicle.getEnginePower() + "</td><td>" + decorated.getEnginePower() + "</td></tr>" +
                "<tr><td>Скорость</td><td>" + vehicle.getMaxSpeed() + "</td><td>" + decorated.getMaxSpeed() + "</td></tr>" +
                "<tr><td>Цена</td><td>" + vehicle.getPrice() + "</td><td>" + decorated.getPrice() + "</td></tr>" +
                "</table><br/>" +
                "<b>Улучшения:</b><br/>" +
                decorated.getDescription().substring(vehicle.getDescription().length()) +
                "</body></html>";

        JOptionPane.showMessageDialog(this, message, "Сравнение версий", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showXMLDialog() {
        XMLJSONDialog dialog = new XMLJSONDialog(this, "XML операции", "xml");
        dialog.setVisible(true);
    }

    private void showJSONDialog() {
        XMLJSONDialog dialog = new XMLJSONDialog(this, "JSON операции", "json");
        dialog.setVisible(true);
    }

    private void updateStatus() {
        int totalVehicles = tableModel.getRowCount();
        int decoratedCount = (int) decoratedVehicles.stream().map(VehicleComponent::getId).distinct().count();
        countLabel.setText(String.format("Всего ТС: %d | Улучшенных: %d", totalVehicles, decoratedCount));
    }
}
