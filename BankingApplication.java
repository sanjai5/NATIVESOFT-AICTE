import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class BankAccount {
    private String accountNumber;
    private String accountHolder;
    private double balance;

    public BankAccount(String accountNumber, String accountHolder) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = 0;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        }
    }
}

public class BankingApplication {

    private static ArrayList<BankAccount> accounts = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BankingApplication::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Banking Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));

        JLabel nameLabel = new JLabel("Account Holder Name:");
        JTextField nameField = new JTextField();

        JLabel numberLabel = new JLabel("Account Number:");
        JTextField numberField = new JTextField();

        JLabel balanceLabel = new JLabel("Initial Balance:");
        JTextField balanceField = new JTextField();

        JButton createButton = new JButton("Create Account");
        JButton checkButton = new JButton("Check Account");

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String number = numberField.getText();
                String balanceText = balanceField.getText();

                if (!name.isEmpty() && !number.isEmpty()) {
                    double initialBalance = 0;
                    try {
                        initialBalance = Double.parseDouble(balanceText);
                    } catch (NumberFormatException ex) {
                        resultArea.setText("Invalid balance amount. Setting to 0.");
                    }

                    BankAccount account = new BankAccount(number, name);
                    account.deposit(initialBalance);
                    accounts.add(account);
                    resultArea.setText("Account created successfully for " + name);
                } else {
                    resultArea.setText("Please enter valid details.");
                }
            }
        });

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String number = numberField.getText();

                BankAccount account = findAccount(number);
                if (account != null) {
                    resultArea.setText("Account Holder: " + account.getAccountHolder() +
                            "\nAccount Number: " + account.getAccountNumber() +
                            "\nCurrent Balance: " + account.getBalance());
                } else {
                    resultArea.setText("Account not found.");
                }
            }
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(numberLabel);
        panel.add(numberField);
        panel.add(balanceLabel);
        panel.add(balanceField);
        panel.add(createButton);
        panel.add(checkButton);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static BankAccount findAccount(String accountNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
}
