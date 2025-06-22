package org.example;
import  java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UserDao {
    private Connection connection;
    private String nameTable;

    public UserDao(Connection conn) {
        connection = conn;
    }
    public void create(String name) throws SQLException {
        nameTable = name;
        String sql = "CREATE TABLE " + name + " (id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100), phone VARCHAR(20))";
        try (var statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public void save(User user) throws SQLException {
        if (user.getId() == null) {
            var sql = "INSERT INTO users (name, phone) VALUES (?, ?)";
            try (var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getPhone());
                preparedStatement.executeUpdate();
                var generatedKeys = preparedStatement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving an entity");
                }
            }
        } else {
            System.out.println("Record is aviable. Update manual");
        }
    }

    public Optional<User> find(Long id) throws SQLException {
        var sql = "SELECT * FROM " + nameTable + " WHERE id = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1,id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var phone = resultSet.getString("phone");
                var user = new User(name, phone);
                user.setId(id);
                return Optional.of(user);
            }
            return Optional.empty();
        }
    }

}
