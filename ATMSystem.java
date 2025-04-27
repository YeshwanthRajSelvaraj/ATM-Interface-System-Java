    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.*;
    import java.util.HashMap;

    // Account class to store user information
    class Account {
        protected String accountNumber;
        protected String pin;
        protected double balance;

        public Account(String accountNumber, String pin, double balance) {
            this.accountNumber = accountNumber;
            this.pin = pin;
            this.balance = balance;
        }

        public boolean authenticate(String enteredPin) {
            return this.pin.equals(enteredPin);
        }

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            if (amount > 0) {
                balance += amount;
            }
        }

        public boolean withdraw(double amount) {
            if (amount > 0 && amount <= balance) {
                balance -= amount;
                return true;
            }
            return false;
        }

        public void changePin(String newPin) {
            if (newPin != null && !newPin.trim().isEmpty()) {
                this.pin = newPin;
            }
        }
    }

    // ATM GUI (Login Screen)
    class ATMGUI extends JFrame implements ActionListener {
        private JTextField cardNumberField;
        private JPasswordField pinField;
        private JButton loginButton;
        private JLabel statusLabel;
        private HashMap<String, Account> accounts;

        public ATMGUI() {
            setTitle("ATM Interface");
            setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-Screen Mode
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setResizable(true);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(new Color(30, 30, 80)); // Dark Theme
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(20, 20, 20, 20);

            accounts = new HashMap<>();
            accounts.put("123456", new Account("123456", "1234", 5000));
            accounts.put("654321", new Account("654321", "4321", 3000));

            JLabel titleLabel = new JLabel("Welcome to ATM");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
            titleLabel.setForeground(Color.WHITE);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(titleLabel, gbc);

            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = 1;
            JLabel cardLabel = new JLabel("Card Number:");
            cardLabel.setForeground(Color.WHITE);
            cardLabel.setFont(new Font("Arial", Font.BOLD, 18));
            panel.add(cardLabel, gbc);

            gbc.gridx = 1;
            cardNumberField = new JTextField(20);
            cardNumberField.setFont(new Font("Arial", Font.PLAIN, 18));
            panel.add(cardNumberField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            JLabel pinLabel = new JLabel("PIN:");
            pinLabel.setForeground(Color.WHITE);
            pinLabel.setFont(new Font("Arial", Font.BOLD, 18));
            panel.add(pinLabel, gbc);

            gbc.gridx = 1;
            pinField = new JPasswordField(20);
            pinField.setFont(new Font("Arial", Font.PLAIN, 18));
            panel.add(pinField, gbc);

            gbc.gridx = 1;
            gbc.gridy = 3;
            loginButton = new JButton("Login");
            loginButton.setFont(new Font("Arial", Font.BOLD, 18));
            loginButton.setBackground(Color.ORANGE);
            loginButton.setForeground(Color.BLACK);
            loginButton.addActionListener(this);
            panel.add(loginButton, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            statusLabel = new JLabel("Enter credentials to login", SwingConstants.CENTER);
            statusLabel.setForeground(Color.YELLOW);
            statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
            panel.add(statusLabel, gbc);

            add(panel);
            setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String cardNumber = cardNumberField.getText().trim();
            String pin = new String(pinField.getPassword()).trim();

            if (cardNumber.isEmpty() || pin.isEmpty()) {
                statusLabel.setText("Please fill all fields!");
                return;
            }

            if (accounts.containsKey(cardNumber)) {
                Account userAccount = accounts.get(cardNumber);
                if (userAccount.authenticate(pin)) {
                    new ATMOperations(userAccount, this);
                    setVisible(false);
                } else {
                    statusLabel.setText("Invalid PIN!");
                }
            } else {
                statusLabel.setText("Invalid Account!");
            }
        }

        public void resetLoginFields() {
            cardNumberField.setText("");
            pinField.setText("");
            statusLabel.setText("Enter credentials to login");
        }
    }

    // ATM Operations (Main Menu)
    class ATMOperations extends JFrame implements ActionListener {
        private Account account;
        private ATMGUI parentFrame;
        private JLabel balanceLabel;
        private JButton withdrawButton, depositButton, checkBalanceButton, changePinButton, logoutButton;

        public ATMOperations(Account account, ATMGUI parent) {
            this.account = account;
            this.parentFrame = parent;

            setTitle("ATM Menu");
            setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-Screen Mode
            setLocationRelativeTo(null);
            setResizable(true);

            JPanel panel = new JPanel(new GridLayout(6, 1, 20, 20));
            panel.setBackground(new Color(30, 30, 80)); // Dark Theme
            panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

            balanceLabel = new JLabel("Current Balance: ₹" + String.format("%.2f", account.getBalance()), SwingConstants.CENTER);
            balanceLabel.setForeground(Color.WHITE);
            balanceLabel.setFont(new Font("Arial", Font.BOLD, 24));
            panel.add(balanceLabel);

            withdrawButton = createStyledButton("Withdraw");
            depositButton = createStyledButton("Deposit");
            checkBalanceButton = createStyledButton("Check Balance");
            changePinButton = createStyledButton("Change PIN");
            logoutButton = createStyledButton("Logout");

            JButton[] buttons = {withdrawButton, depositButton, checkBalanceButton, changePinButton, logoutButton};
            for (JButton btn : buttons) {
                btn.addActionListener(this);
                panel.add(btn);
            }

            add(panel);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }

        private JButton createStyledButton(String text) {
            JButton button = new JButton(text);
            button.setBackground(new Color(255, 140, 0)); // Orange
            button.setForeground(Color.BLACK);
            button.setFocusPainted(false);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setPreferredSize(new Dimension(200, 50));
            return button;
        }

        private void updateBalanceLabel() {
            balanceLabel.setText("Current Balance: ₹" + String.format("%.2f", account.getBalance()));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (e.getSource() == withdrawButton) {
                    String amountStr = JOptionPane.showInputDialog("Enter amount to withdraw:");
                    double amount = Double.parseDouble(amountStr);
                    if (account.withdraw(amount)) {
                        updateBalanceLabel();
                        JOptionPane.showMessageDialog(this, "Withdrawal Successful!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid amount or Insufficient Balance!");
                    }
                } else if (e.getSource() == depositButton) {
                    String amountStr = JOptionPane.showInputDialog("Enter amount to deposit:");
                    double amount = Double.parseDouble(amountStr);
                    account.deposit(amount);
                    updateBalanceLabel();
                    JOptionPane.showMessageDialog(this, "Deposit Successful!");
                } else if (e.getSource() == checkBalanceButton) {
                    JOptionPane.showMessageDialog(this, "Your Balance: ₹" + String.format("%.2f", account.getBalance()));
                } else if (e.getSource() == changePinButton) {
                    String newPin = JOptionPane.showInputDialog("Enter new PIN:");
                    account.changePin(newPin);
                    JOptionPane.showMessageDialog(this, "PIN Changed Successfully!");
                } else if (e.getSource() == logoutButton) {
                    dispose();
                    parentFrame.resetLoginFields();
                    parentFrame.setVisible(true);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number!");
            }
        }
    }

    // Main class
    public class ATMSystem {
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> new ATMGUI());
        }
    }
