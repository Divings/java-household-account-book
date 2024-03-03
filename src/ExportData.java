import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class ExportData {

    private static final String DATABASE_URL = "jdbc:sqlite:expense_tracker.db";
    private static final String SELECT_TRANSACTIONS_QUERY = "SELECT * FROM transactions";

    public static void ExportData() {
        try {
            // SQLite JDBC ドライバを読み込む
            Class.forName("org.sqlite.JDBC");

            // SQLite データベースに接続
            Connection connection = DriverManager.getConnection(DATABASE_URL);

            // SELECT クエリを実行し、結果を取得
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_TRANSACTIONS_QUERY);

            // 結果をファイルに書き出し
            writeResultSetToFile(resultSet, "exported_data.csv");

            // ダイアログ表示
            JOptionPane.showMessageDialog(
                    null,
                    "Data exported successfully.",
                    "Export Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // リソースを解放
            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeResultSetToFile(ResultSet resultSet, String fileName) throws IOException, SQLException {
        FileWriter writer = new FileWriter(fileName);

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // ヘッダー行を書き込み
        for (int i = 1; i <= columnCount; i++) {
            writer.append(metaData.getColumnName(i));
            if (i < columnCount) {
                writer.append(",");
            }
        }
        writer.append("\n");

        // 結果セットの内容をファイルに書き込み
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                writer.append(resultSet.getString(i));
                if (i < columnCount) {
                    writer.append(",");
                }
            }
            writer.append("\n");
        }

        writer.close();
    }
}
