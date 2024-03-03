Expense Tracker App README

This Expense Tracker application is a simple Java Swing-based desktop application that allows users to track their expenses. The application uses SQLite as the database to store transaction data.

How to Run:

1. Ensure you have Java installed on your system.
2. Download the "ExpenseTracker.jar" file from the repository.
3. Open a terminal or command prompt and navigate to the directory containing the JAR file.
4. Run the following command: java -jar ExpenseTracker.jar<br>
Note: Please copy the dependency libraries to the lib directory<br>
See lib/lib-list.txt for details

Features:

1. **Add Transaction:** Enter the transaction details, including amount, description, and date, and click the "Add Transaction" button to add it to the database.

2. **Display Transactions:** Click the "Display Transactions" button to view a list of all transactions in the JTextArea.

User Interface:

- The application window contains input fields for amount, description, and date.
- "Add Transaction" and "Display Transactions" buttons perform the respective actions.
- The JTextArea displays a formatted list of transactions.

Database:

- The application uses SQLite as the database, and the transactions table is created with columns: id, amount, description, and date.

Fonts:

- UTF-8 encoding is used for input fields and JTextArea to support international characters.

Note: To customize the application or contribute to development, refer to the source code.

Author: Divings
Date: 2024-03-03
