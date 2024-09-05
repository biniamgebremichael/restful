package com.friday.persist;

import com.friday.User;
import java.sql.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqliteOnFileDAO implements Perister {


    String dbDir = "/data";
    private final String createTable = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, prefix TEXT, firstName TEXT NOT NULL, middleName TEXT, lastName TEXT NOT NULL, suffix TEXT , email TEXT NOT NULL UNIQUE, phone TEXT )";
    private final String url;

    private static final Logger LOGGER = Logger.getLogger(SqliteOnFileDAO.class.getName());


    public SqliteOnFileDAO(FileHandler fileHandler) {
        LOGGER.addHandler(fileHandler);
        LOGGER.setLevel(Level.ALL);

        File dbFile = new File(dbDir, "firday.db");
        url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(this.url);
            PreparedStatement pstmt = conn.prepareStatement(createTable);
            pstmt.execute();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize database", e);
        }

        return conn;
    }

    @Override
    public int add(User user) {
        String sql = "INSERT INTO users( prefix , firstName   , middleName , lastName , suffix , email, phone) VALUES(?, ?, ?, ?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.prefix());
            pstmt.setString(2, user.firstName());
            pstmt.setString(3, user.middleName());
            pstmt.setString(4, user.lastName());
            pstmt.setString(5, user.suffix());
            pstmt.setString(6, user.email());
            pstmt.setString(7, user.phone());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to add user", e);
            return 0;
        }

    }

    @Override
    public int update(User user) {
        String sql = "UPDATE  users set prefix=? , firstName=?   , middleName=? , lastName=? , suffix=? , phone=? WHERE email = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.prefix());
            pstmt.setString(2, user.firstName());
            pstmt.setString(3, user.middleName());
            pstmt.setString(4, user.lastName());
            pstmt.setString(5, user.suffix());
            pstmt.setString(6, user.phone());
            pstmt.setString(7, user.email());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to update user", e);
            return 0;
        }

    }

    @Override
    public User get(String email) {
        String sql = "Select * from users where email = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return getUser(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to query user table", e);

        }
        return null;
    }


    @Override
    public User get(Integer id) {
        String sql = "Select * from users where id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return getUser(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to query user table", e);


        }
        return null;
    }

    @Override
    public int delete(String email) {
        String sql = "delete from users where email = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to query user table", e);

        }
        return 0;
    }


    @Override
    public List<User> get() {
        String sql = "Select * from users";

        try (Connection conn = this.connect()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = getUser(resultSet);
                users.add(user);

            }
            return users;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to query user table", e);

        }
        return null;
    }

    private static User getUser(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getString("prefix")
                , resultSet.getString("firstName")
                , resultSet.getString("middleName")
                , resultSet.getString("lastName")
                , resultSet.getString("suffix")
                , resultSet.getString("email")
                , resultSet.getString("phone"));

    }
}


