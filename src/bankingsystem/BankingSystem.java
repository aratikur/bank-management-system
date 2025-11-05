package bankingsystem;

import java.io.*;
import java.util.ArrayList;

public class BankingSystem {
    private static final String FILE_NAME = "accounts.ser";
    private static ArrayList<Account> accounts = new ArrayList<>();
    private static Account currentAccount = null;

    @SuppressWarnings("unchecked")
    public static void loadAccounts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (ArrayList<Account>) ois.readObject();
        } catch (Exception ignored) {}
    }

    public static void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accounts);
        } catch (IOException ignored) {}
    }

    public static boolean createAccount(String name, String dob, int phone, String password) {
        if (findAccountByPhone(phone) != null) return false;
        accounts.add(new Account(name, dob, phone, password));
        saveAccounts();
        return true;
    }

    public static Account findAccount(int phone, String password) {
        for (Account acc : accounts) {
            if (acc.getPhone() == phone && acc.getPassword().equals(password)) return acc;
        }
        return null;
    }

    public static Account findAccountByPhone(int phone) {
        for (Account acc : accounts) {
            if (acc.getPhone() == phone) return acc;
        }
        return null;
    }

    public static ArrayList<Account> getAccounts() {
        return accounts;
    }

    public static boolean login(int phone, String password) {
        Account acc = findAccount(phone, password);
        if (acc != null) {
            currentAccount = acc;
            return true;
        }
        return false;
    }

    public static void logout() {
        currentAccount = null;
    }

    public static Account getCurrentAccount() {
        return currentAccount;
    }

    public static boolean deposit(double amount) {
        if (currentAccount != null) {
            try {
                currentAccount.deposit((float) amount);
                saveAccounts();
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean withdraw(double amount) {
        if (currentAccount != null && amount > 0 && currentAccount.withdraw((float) amount)) {
            saveAccounts();
            return true;
        }
        return false;
    }

    public static boolean transfer(int toPhone, double amount) {
        if (currentAccount == null || amount <= 0) return false;
        Account toAcc = findAccountByPhone(toPhone);
        if (toAcc != null && currentAccount.withdraw((float) amount)) {
            toAcc.deposit((float) amount);
            saveAccounts();
            return true;
        }
        return false;
    }
}