import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ExpenseTracker extends JFrame {
    private static final String FONTS="UTF-8";
    private static final String DATABASE_URL = "jdbc:sqlite:expense_tracker.db";
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS transactions (id INTEGER PRIMARY KEY AUTOINCREMENT, amount REAL, description TEXT, date TEXT)";
    private static final String INSERT_TRANSACTION_QUERY = "INSERT INTO transactions (amount, description, date) VALUES (?, ?, ?)";
    private static final String SELECT_TRANSACTIONS_QUERY = "SELECT * FROM transactions";

    private JTextField amountField;
    private JTextField descriptionField;
    private JTextField dateField;
    private JTextArea transactionsArea;

    public ExpenseTracker() {
        super("Expense Tracker");

        initUI();
        setupDatabase();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setResizable(true); // サイズ変更禁止
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        // フォントサイズの設定
        Font inputFont = new Font(FONTS, Font.PLAIN, 14);
        
        amountField = new JTextField();
        amountField.setFont(inputFont);

        descriptionField = new JTextField();
        descriptionField.setFont(inputFont);

        dateField = new JTextField();
        dateField.setFont(inputFont);

        JButton addButton = new JButton("Add Transaction");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTransaction();
            }
        });

        JButton displayButton = new JButton("Display Transactions");
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTransactions();
            }
        });

        Font buttonFont = new Font(FONTS, Font.PLAIN, 16); // ボタンのフォント
        addButton.setFont(buttonFont);
        displayButton.setFont(buttonFont);

        transactionsArea = new JTextArea();
        transactionsArea.setEditable(false);
        transactionsArea.setFont(new Font(FONTS, Font.PLAIN, 14)); // テキストエリアのフォント

        Font labelFont = new Font(FONTS, Font.BOLD, 14); // ラベルのフォント
        panel.add(new JLabel("Amount:")).setFont(labelFont);
        panel.add(amountField);
        panel.add(new JLabel("Description:")).setFont(labelFont);
        panel.add(descriptionField);
        panel.add(new JLabel("Date (yyyy-mm-dd):")).setFont(labelFont);
        panel.add(dateField);
        panel.add(addButton);
        panel.add(displayButton);

        add(panel, BorderLayout.NORTH); // パネルを上部に配置

        // transactionsArea をスクロール可能にし、横幅をウィンドウいっぱいに拡張
        int rows = 5;
        int rowHeight = transactionsArea.getFontMetrics(transactionsArea.getFont()).getHeight();
        JScrollPane scrollPane = new JScrollPane(transactionsArea);
        scrollPane.setPreferredSize(new Dimension(getWidth(), rows * rowHeight - 3 * transactionsArea.getFontMetrics(transactionsArea.getFont()).getHeight()));
        add(scrollPane, BorderLayout.CENTER); // スクロールペインを中央に配置
    }

    private void setupDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(DATABASE_URL);
            connection.createStatement().executeUpdate(CREATE_TABLE_QUERY);
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void addTransaction() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String description = descriptionField.getText();
            String date = dateField.getText();

            Connection connection = DriverManager.getConnection(DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRANSACTION_QUERY);
            preparedStatement.setDouble(1, amount);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, date);
            preparedStatement.executeUpdate();

            connection.close();

            displayTransactions();
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check the fields.");
        }
    }

private void displayTransactions() {
    try {
        Connection connection = DriverManager.getConnection(DATABASE_URL);
        ResultSet resultSet = connection.createStatement().executeQuery(SELECT_TRANSACTIONS_QUERY);

        // 表示用の文字列を格納する StringBuilder
        StringBuilder transactionsText = new StringBuilder("ID\t金額\t説明\t\t日付\n");

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            double amount = resultSet.getDouble("amount");
            String description = resultSet.getString("description");
            String date = resultSet.getString("date");

            // 日本語文字列の追加
            transactionsText.append(id).append("\t").append(amount).append("\t").append(description).append("\t").append(date).append("\n");
        }

        // JTextArea に表示
        transactionsArea.setText(transactionsText.toString());

        connection.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ExpenseTracker();
            }
        });
    }
}
