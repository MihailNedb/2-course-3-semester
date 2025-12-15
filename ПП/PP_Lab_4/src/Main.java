import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        TransportCompany.createTestData();


        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new VehicleTableGUI().setVisible(true);
        });
    }
}