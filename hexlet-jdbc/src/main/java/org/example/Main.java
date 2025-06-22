package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {


        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {

            var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
            try (var statement = conn.createStatement()) {
                statement.execute(sql);
            }//statement.close(); //переход на try

            //var sql12 = "INSERT INTO users (username, phone) VALUES ('tommy', '123456')"; //переходн на плейсхолдеры
            var sql12 = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql12)) {
                preparedStatement.setString(1, "Tommy");
                preparedStatement.setString(2, "3333333");
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Maria");
                preparedStatement.setString(2, "4444444");
                preparedStatement.executeUpdate();
            //try var statement2 = conn.createStatement()) {
               // statement2.executeUpdate(sql12);
            } //statement2.close();  //переход на try

            var sql13 = "SELECT * FROM users";
            try (var statement3 = conn.createStatement()) {
                var resultSet = statement3.executeQuery(sql13);
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("username"));
                    System.out.println(resultSet.getString("phone"));
                }
            }//statement3.close(); //переход на try
        } //conn.close();


        //UserDao
        System.out.println("-------------------UserDao------------------");
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test");
        new UserDao(conn);
        var Vasya = new User("Vasya", "55555");
        var Sveta = new User("Sveta", "44444");
        var Masha = new User("Masha", "33333");

        var dao = new UserDao(conn);


        dao.create("users");
        dao.save(Vasya);
        dao.save(Sveta);
        dao.save(Masha);

        System.out.println(dao.find(1L).get());
        System.out.println(dao.find(3L).get());
     }
}