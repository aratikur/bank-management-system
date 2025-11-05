import bankingsystem.BankingSystem;
import bankingsystem.Account;

import javax.swing.*;
import java.awt.*;

public class firstFrame extends JFrame {
    private JTextField nameField, dobField, phoneField;
    private JPasswordField passwordField;
    private JTextArea outputArea;

    public firstFrame() {
        setTitle("Banking System");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load saved accounts
        BankingSystem.loadAccounts();

        // Components
        JLabel nameLabel = new JLabel("Name:");
        JLabel dobLabel = new JLabel("DOB (dd/mm/yyyy):");
        JLabel phoneLabel = new JLabel("Phone:");
        JLabel passwordLabel = new JLabel("Password:");

        nameField = new JTextField(15);
        dobField = new JTextField(15);
        phoneField = new JTextField(15);
        passwordField = new JPasswordField(15);

        JButton createBtn = new JButton("Create Account");
        JButton loginBtn = new JButton("Login");

        outputArea = new JTextArea(8, 40);
        outputArea.setEditable(false);

        // Layout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(nameLabel, gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(dobLabel, gbc);
        gbc.gridx = 1; panel.add(dobField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(phoneLabel, gbc);
        gbc.gridx = 1; panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(passwordLabel, gbc);
        gbc.gridx = 1; panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(createBtn, gbc);
        gbc.gridx = 1; panel.add(loginBtn, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JScrollPane(outputArea), gbc);

        add(panel);

        // Actions
        createBtn.addActionListener(e -> createAccount());
        loginBtn.addActionListener(e -> loginAccount());
    }

    private void createAccount() {
        try {
            String name = nameField.getText();
            String dob = dobField.getText();
            int phone = Integer.parseInt(phoneField.getText());
            String password = new String(passwordField.getPassword());

            if (BankingSystem.createAccount(name, dob, phone, password)) {
                outputArea.setText("Account created successfully!");
            } else {
                outputArea.setText("Account with this phone already exists.");
            }
        } catch (NumberFormatException ex) {
            outputArea.setText("Invalid phone number.");
        }
    }

    private void loginAccount() {
        try {
            int phone = Integer.parseInt(phoneField.getText());
            String password = new String(passwordField.getPassword());

            Account acc = BankingSystem.findAccount(phone, password);
            if (acc != null) {
                outputArea.setText("Welcome, " + acc.getName() + "!\nBalance: " + acc.getBalance());
            } else {
                outputArea.setText("Invalid credentials.");
            }
        } catch (NumberFormatException ex) {
            outputArea.setText("Invalid phone number.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new firstFrame().setVisible(true));
    }
}
