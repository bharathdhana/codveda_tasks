package com.bharath.library.dao;

import com.bharath.library.config.DBConnection;
import com.bharath.library.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public void addUser(User user) throws SQLException {
        String sql = """
                insert into users(name, email) values (?, ?)
                """;

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
         ps.setString(1, user.getName());
         ps.setString(2, user.getEmail());
         ps.executeUpdate();

         System.out.println("User registered successfully!");
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "select * from users order by user_id";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
              users.add(mapUser(rs));
            }
        }
        return users;
    }

    public User getUserById(int id) throws SQLException {
        String sql = "select * from users where user_id = ?";
        try(Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapUser(rs) : null;
            }
        }
    }

    public boolean updateUser(User user) throws SQLException {
        String sql = """
                update users set name = ?, email = ?
                where user_id = ?
                """;
        try(Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setInt(3, user.getUserId());

           return ps.executeUpdate() == 1;
        }
     }

     public boolean deleteUser(int id) throws SQLException {
        String sql = """
                delete from users u where u.user_id = ?
                and not exists(select 1 from transactions t where t.user_id = u.user_id)
                """;

        try(Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
     }

     private User mapUser(ResultSet rs) throws SQLException {
         Timestamp created = rs.getTimestamp("created_at");
         return new User(rs.getInt("user_id"),
                 rs.getString("name"),
                 rs.getString("email"),
                 created == null ? null : created.toLocalDateTime());
     }
}
