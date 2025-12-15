import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDecoratorDialog extends JDialog {

    private Vehicle baseVehicle;
    private VehicleComponent decoratedVehicle;
    private boolean confirmed = false;

    private JCheckBox turboCheck, navCheck, audioCheck, spoilerCheck, armorCheck;
    private JLabel resultLabel;

    public VehicleDecoratorDialog(JFrame parent, Vehicle vehicle) {
        super(parent, "Улучшение транспортного средства", true);
        this.baseVehicle = vehicle;
        this.decoratedVehicle = vehicle;

        setSize(850, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initComponents();
        updateResult();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        mainPanel.add(createSelectionPanel(), BorderLayout.CENTER);
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        resultLabel = new JLabel();
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultLabel.setForeground(Color.BLACK);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(resultLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    // ---------- ПАНЕЛЬ ВЫБОРА УЛУЧШЕНИЙ ----------
    private JPanel createSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel title = new JLabel("Выберите улучшения для: " + baseVehicle.getId());
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        turboCheck = createRow(panel, gbc, row++,
                "Турбонаддув",
                "+50 л.с.",
                "5000 $");

        navCheck = createRow(panel, gbc, row++,
                "Навигационная система",
                "GPS Pro",
                "1500 $");

        audioCheck = createRow(panel, gbc, row++,
                "Премиум аудиосистема",
                "Bose",
                "3000 $");

        spoilerCheck = createRow(panel, gbc, row++,
                "Спойлер",
                "+20 км/ч",
                "2500 $");

        armorCheck = createRow(panel, gbc, row++,
                "Бронирование",
                "Уровень 3",
                "10000 $");

        return panel;
    }

    // ---------- ОДНА СТРОКА УЛУЧШЕНИЯ ----------
    private JCheckBox createRow(JPanel panel, GridBagConstraints gbc, int row,
                                String name, String description, String price) {

        JCheckBox checkBox = new JCheckBox(name);
        checkBox.setForeground(Color.BLACK);
        checkBox.addActionListener(e -> updateResult());

        JLabel descLabel = new JLabel(description);
        descLabel.setForeground(Color.BLACK);

        JLabel priceLabel = new JLabel(price);
        priceLabel.setForeground(Color.BLACK);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 13));

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(checkBox, gbc);

        gbc.gridx = 1;
        panel.add(descLabel, gbc);

        gbc.gridx = 2;
        panel.add(priceLabel, gbc);

        return checkBox;
    }

    // ---------- НИЖНЯЯ ПАНЕЛЬ ----------
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton resetButton = new JButton("Сбросить");
        resetButton.setForeground(Color.BLACK);
        resetButton.addActionListener(e -> reset());

        JButton cancelButton = new JButton("Отмена");
        cancelButton.setForeground(Color.BLACK);
        cancelButton.addActionListener(e -> onCancel());

        JButton okButton = new JButton("Применить");
        okButton.setForeground(Color.BLACK);
        okButton.addActionListener(e -> onOk());

        panel.add(resetButton);
        panel.add(cancelButton);
        panel.add(okButton);

        return panel;
    }

    // ---------- ЛОГИКА УЛУЧШЕНИЙ ----------
    private void updateDecoratedVehicle() {
        VehicleComponent result = baseVehicle;

        if (turboCheck.isSelected()) {
            result = new TurboChargerDecorator(result, 50, 5000);
        }

        if (navCheck.isSelected()) {
            result = new NavigationSystemDecorator(result, "GPS Pro", 1500);
        }

        if (audioCheck.isSelected()) {
            result = new PremiumSoundSystemDecorator(result, "Bose", 3000);
        }

        if (spoilerCheck.isSelected()) {
            result = new SpoilerDecorator(result, "Карбон", 2500, 20);
        }

        if (armorCheck.isSelected()) {
            result = new ArmorDecorator(result, "Уровень 3", 10000, 20);
        }

        decoratedVehicle = result;
    }

    private void updateResult() {
        updateDecoratedVehicle();

        double priceDiff = decoratedVehicle.getPrice() - baseVehicle.getPrice();

        resultLabel.setText(String.format(
                "Итоговая цена: %.2f $",
                decoratedVehicle.getPrice()
        ));
    }

    private void reset() {
        turboCheck.setSelected(false);
        navCheck.setSelected(false);
        audioCheck.setSelected(false);
        spoilerCheck.setSelected(false);
        armorCheck.setSelected(false);
        updateResult();
    }

    private void onOk() {
        updateDecoratedVehicle();
        confirmed = true;
        dispose();
    }

    private void onCancel() {
        confirmed = false;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public VehicleComponent getDecoratedVehicle() {
        return decoratedVehicle;
    }
}

