package src.main.java.tutorial.example;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;


public class MySQL {

    private static final String url = "jdbc:mysql://localhost:3306/mydb";
    private static final String user = "root";
    private static final String password = "p@ssw0rd2281337AA*";


    // Переменные Java DataBaseConnectivity для открытия и управления соединениями
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;


    public static void main(String[] args) {
        String query = "select count(*) from files";

        try {
            con = DriverManager.getConnection(url, user, password);

            stmt = con.createStatement();

            rs = stmt.executeQuery(query);

            while (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Общее количество файлов в таблице : " + count);

            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {con.close();} catch (SQLException sqlException) {}
            try {stmt.close();} catch (SQLException sqlException) {}
            try {rs.close();} catch (SQLException sqlException) {}
        }
    }
}
