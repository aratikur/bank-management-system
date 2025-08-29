package bankingsystem;

import java.io.Serializable;

public class Account implements Serializable {
    private String name;
    private String dob;
    private String password;
    private int phone;
    private float balance;

    public Account(String name, String dob, int phone, String password) {
        this.name = name;
        this.dob = dob;
        this.phone = phone;
        this.password = password;
        this.balance = 0.0f;
    }

    public String getName() { return name; }
    public String getDob() { return dob; }
    public String getPassword() { return password; }
    public int getPhone() { return phone; }
    public float getBalance() { return balance; }

    public void deposit(float amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        this.balance += amount;
    }

    public boolean withdraw(float amount) {
        if (amount <= 0) {
            return false; // no funny business
        }
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}