package bankingsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

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
        
        // Create a clickable date field with calendar popup
        JTextField dobField = new JTextField(15);
        dobField.setEditable(false);
        dobField.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        JButton calendarBtn = new JButton("Select");
        calendarBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        calendarBtn.setPreferredSize(new Dimension(70, 25));
        calendarBtn.setToolTipText("Select Date");
        
        JPanel dobPanel = new JPanel(new BorderLayout(5, 0));
        dobPanel.add(dobField, BorderLayout.CENTER);
        dobPanel.add(calendarBtn, BorderLayout.EAST);
        
        JTextField phoneField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        JButton createBtn = new JButton("Create Account");
        createBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        outputArea = new JTextArea(6, 30);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("DOB (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1; panel.add(dobPanel, gbc);        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; panel.add(passwordField, gbc);

        gbc.gridx = 1; gbc.gridy = 4; panel.add(createBtn, gbc);
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JScrollPane(outputArea), gbc);

        // Calendar button action - opens a date picker dialog
        calendarBtn.addActionListener(e -> {
            Date selectedDate = showDatePickerDialog(this);
            if (selectedDate != null) {
                dobField.setText(new SimpleDateFormat("dd/MM/yyyy").format(selectedDate));
            }
        });

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

    private Date showDatePickerDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Select Date of Birth", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(parent);

        final Date[] selectedDate = {null};
        
        // Create calendar components
        Calendar cal = Calendar.getInstance();
        
        JPanel topPanel = new JPanel(new FlowLayout());
        String[] months = {"January", "February", "March", "April", "May", "June", 
                          "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthCombo = new JComboBox<>(months);
        monthCombo.setSelectedIndex(cal.get(Calendar.MONTH));
        
        JComboBox<Integer> yearCombo = new JComboBox<>();
        int currentYear = cal.get(Calendar.YEAR);
        for (int i = currentYear - 100; i <= currentYear; i++) {
            yearCombo.addItem(i);
        }
        yearCombo.setSelectedItem(currentYear - 20); // Default to 20 years ago
        
        topPanel.add(new JLabel("Month:"));
        topPanel.add(monthCombo);
        topPanel.add(new JLabel("Year:"));
        topPanel.add(yearCombo);
        
        // Create day selection panel with proper sizing
        JPanel dayPanel = new JPanel(new GridLayout(7, 7, 5, 5));
        dayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : dayNames) {
            JLabel lbl = new JLabel(day, JLabel.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 13));
            dayPanel.add(lbl);
        }
        
        JButton[] dayButtons = new JButton[42];
        for (int i = 0; i < 42; i++) {
            dayButtons[i] = new JButton();
            dayButtons[i].setFont(new Font("Arial", Font.PLAIN, 13));
            dayButtons[i].setPreferredSize(new Dimension(50, 40));
            dayButtons[i].setMargin(new Insets(2, 2, 2, 2));
            final int index = i;
            dayButtons[i].addActionListener(e -> {
                if (!dayButtons[index].getText().isEmpty()) {
                    int day = Integer.parseInt(dayButtons[index].getText());
                    cal.set(Calendar.YEAR, (Integer) yearCombo.getSelectedItem());
                    cal.set(Calendar.MONTH, monthCombo.getSelectedIndex());
                    cal.set(Calendar.DAY_OF_MONTH, day);
                    selectedDate[0] = cal.getTime();
                    dialog.dispose();
                }
            });
            dayPanel.add(dayButtons[i]);
        }
        
        Runnable updateCalendar = () -> {
            cal.set(Calendar.YEAR, (Integer) yearCombo.getSelectedItem());
            cal.set(Calendar.MONTH, monthCombo.getSelectedIndex());
            cal.set(Calendar.DAY_OF_MONTH, 1);
            
            int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
            int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            
            for (int i = 0; i < 42; i++) {
                dayButtons[i].setText("");
                dayButtons[i].setEnabled(false);
                dayButtons[i].setBackground(null);
            }
            
            for (int day = 1; day <= daysInMonth; day++) {
                int buttonIndex = firstDayOfWeek + day - 1;
                dayButtons[buttonIndex].setText(String.valueOf(day));
                dayButtons[buttonIndex].setEnabled(true);
            }
        };
        
        monthCombo.addActionListener(e -> updateCalendar.run());
        yearCombo.addActionListener(e -> updateCalendar.run());
        updateCalendar.run();
        
        JPanel buttonPanel = new JPanel();
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelBtn);
        
        dialog.add(topPanel, BorderLayout.NORTH);
        dialog.add(dayPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
        return selectedDate[0];
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankingSystemGUI().setVisible(true));
    }
}