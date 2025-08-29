package bankingsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class BankingSystemGUI extends JFrame {

    private JPanel contentPanel;
    private JTextArea outputArea;

    public BankingSystemGUI() {
        setTitle("Smart Banking System");
        setSize(750, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        BankingSystem.loadAccounts();

        JLabel headerLabel = new JLabel("Banking Management System", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(headerLabel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] menuItems = {
            "Create Account", "Login", "Deposit", "Withdraw",
            "Transfer", "Status", "Show All Accounts"
        };
        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            btn.setFocusPainted(false);
            btn.addActionListener(e -> switchPanel(item));
            menuPanel.add(btn);
        }
        add(menuPanel, BorderLayout.WEST);

        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        contentPanel.add(createAccountPanel(), "Create Account");
        contentPanel.add(loginPanel(), "Login");
        contentPanel.add(depositPanel(), "Deposit");
        contentPanel.add(withdrawPanel(), "Withdraw");
        contentPanel.add(transferPanel(), "Transfer");
        contentPanel.add(statusPanel(), "Status");
        contentPanel.add(showAllPanel(), "Show All Accounts");

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createAccountPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGBC();

        JTextField nameField = new JTextField(15);
        JTextField dobField = new JTextField(15);
        JTextField phoneField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        JButton createBtn = new JButton("Create Account");
        createBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        outputArea = new JTextArea(6, 30);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("DOB (dd/mm/yyyy):"), gbc);
        gbc.gridx = 1; panel.add(dobField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; panel.add(passwordField, gbc);

        gbc.gridx = 1; gbc.gridy = 4; panel.add(createBtn, gbc);
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JScrollPane(outputArea), gbc);

        createBtn.addActionListener((ActionEvent e) -> {
            try {
                String name = nameField.getText();
                String dob = dobField.getText();
                int phone = Integer.parseInt(phoneField.getText());
                String password = new String(passwordField.getPassword());

                if (BankingSystem.createAccount(name, dob, phone, password)) {
                    outputArea.setText("✅ Account created successfully!");
                } else {
                    outputArea.setText("❌ Account with this phone already exists.");
                }
            } catch (NumberFormatException ex) {
                outputArea.setText("⚠ Invalid phone number.");
            }
        });

        return panel;
    }

    private JPanel loginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGBC();

        JTextField phoneField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JTextArea resultArea = new JTextArea(6, 30);
        resultArea.setEditable(false);

        JButton loginBtn = new JButton("Login");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; panel.add(phoneField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; panel.add(passwordField, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(loginBtn, gbc);
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JScrollPane(resultArea), gbc);

        loginBtn.addActionListener(e -> {
            try {
                int phone = Integer.parseInt(phoneField.getText());
                String password = new String(passwordField.getPassword());
                if (BankingSystem.login(phone, password)) {
                    Account acc = BankingSystem.getCurrentAccount();
                    resultArea.setText("✅ Login successful.\nWelcome, " + acc.getName() + "\nBalance: " + acc.getBalance());
                } else {
                    resultArea.setText("❌ Invalid credentials.");
                }
            } catch (NumberFormatException ex) {
                resultArea.setText("⚠ Invalid phone number.");
            }
        });

        return panel;
    }

    private JPanel depositPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGBC();

        JTextField amountField = new JTextField(15);
        JTextArea resultArea = new JTextArea(6, 30);
        resultArea.setEditable(false);
        JButton depositBtn = new JButton("Deposit");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1; panel.add(amountField, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(depositBtn, gbc);
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JScrollPane(resultArea), gbc);

        depositBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (BankingSystem.deposit(amount)) {
                    resultArea.setText("✅ Deposit successful!\nAmount: " + amount);
                } else {
                    resultArea.setText("❌ Invalid deposit amount or login required.");
                }
            } catch (NumberFormatException ex) {
                resultArea.setText("⚠ Invalid amount.");
            }
        });

        return panel;
    }

    private JPanel withdrawPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGBC();

        JTextField amountField = new JTextField(15);
        JTextArea resultArea = new JTextArea(6, 30);
        resultArea.setEditable(false);
        JButton withdrawBtn = new JButton("Withdraw");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1; panel.add(amountField, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(withdrawBtn, gbc);
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JScrollPane(resultArea), gbc);

        withdrawBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (BankingSystem.withdraw(amount)) {
                    resultArea.setText("✅ Withdrawal successful!\nAmount: " + amount);
                } else {
                    resultArea.setText("❌ Login required, invalid amount, or insufficient funds.");
                }
            } catch (NumberFormatException ex) {
                resultArea.setText("⚠ Invalid amount.");
            }
        });

        return panel;
    }

    private JPanel transferPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGBC();

        JTextField toPhoneField = new JTextField(15);
        JTextField amountField = new JTextField(15);
        JTextArea resultArea = new JTextArea(6, 30);
        resultArea.setEditable(false);
        JButton transferBtn = new JButton("Transfer");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("To Phone:"), gbc);
        gbc.gridx = 1; panel.add(toPhoneField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1; panel.add(amountField, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(transferBtn, gbc);
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JScrollPane(resultArea), gbc);

        transferBtn.addActionListener(e -> {
            try {
                int toPhone = Integer.parseInt(toPhoneField.getText());
                double amount = Double.parseDouble(amountField.getText());
                if (BankingSystem.transfer(toPhone, amount)) {
                    resultArea.setText("✅ Transfer successful!\nAmount: " + amount);
                } else {
                    resultArea.setText("❌ Transfer failed. Login required, invalid amount, or check balance/account.");
                }
            } catch (NumberFormatException ex) {
                resultArea.setText("⚠ Invalid input.");
            }
        });

        return panel;
    }

    private JPanel statusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea statusArea = new JTextArea();
        statusArea.setEditable(false);
        JButton refreshBtn = new JButton("Refresh");

        refreshBtn.addActionListener(e -> {
            Account acc = BankingSystem.getCurrentAccount();
            if (acc != null) {
                statusArea.setText(
                    "👤 Name: " + acc.getName() +
                    "\n📞 Account Number: 0" + acc.getPhone() +
                    "\n🎂 Date of Birth: " + acc.getDob() +
                    "\n💰 Current Balance: " + acc.getBalance()
                );
            } else {
                statusArea.setText("⚠ No user logged in.");
            }
        });

        panel.add(new JScrollPane(statusArea), BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel showAllPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea allAccountsArea = new JTextArea();
        allAccountsArea.setEditable(false);
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> {
            ArrayList<Account> list = BankingSystem.getAccounts();
            StringBuilder sb = new StringBuilder();
            for (Account acc : list) {
                sb.append(acc.getName()).append(" - 0").append(acc.getPhone()).append("\n");
            }
            allAccountsArea.setText(sb.toString());
        });
        panel.add(new JScrollPane(allAccountsArea), BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);
        return panel;
    }

    private GridBagConstraints createGBC() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private void switchPanel(String name) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankingSystemGUI().setVisible(true));
    }
}