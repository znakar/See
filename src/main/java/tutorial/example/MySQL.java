package src.main.java.tutorial.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQL {

    private static final Logger logger = Logger.getLogger(MySQL.class.getName());

    // Данные для подключения к базе данных
    private static final String URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    /**
     * Метод для получения соединения с базой данных.
     *
     * @return Connection объект соединения с базой данных.
     * @throws SQLException если не удалось установить соединение.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Регистрируем драйвер 
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "MySQL JDBC Driver не найден.", e);
            throw new SQLException("MySQL JDBC Driver не найден.");
        }

        // Устанавливаем соединение
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Метод для тестирования соединения с базой данных.
     * Этот метод можно удалить или вынести в отдельный класс для тестирования.
     */
    public static void testConnection() {
        String query = "SELECT COUNT(*) FROM files";

        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                int count = rs.getInt(1);
                logger.info("Общее количество файлов в таблице: " + count);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка при выполнении запроса.", e);
        }
    }

    /**
     * Точка входа для тестирования класса MySQL.
     */
    public static void main(String[] args) {
        testConnection(); // Вызов метода для тестирования соединения
    }
}
