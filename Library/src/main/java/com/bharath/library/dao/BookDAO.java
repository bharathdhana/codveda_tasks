package com.bharath.library.dao;

import com.bharath.library.config.DBConnection;
import com.bharath.library.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "select * from books order by book_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                books.add(mapBook(rs));
            }
        }
        return books;
    }

    public Book getBookById(int id) throws SQLException {
        String sql = "select * from books where book_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapBook(rs) : null;
                }
            }
        }

    public void addBook(Book book) throws SQLException {
        String sql = "insert into books (title, author, isbn, total_copies, available_copies) " +
                "values (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setInt(4, book.getTotalCopies());
            ps.setInt(5, book.getAvailableCopies());
            ps.executeUpdate();
            System.out.println("Book added Successfully");
        }
    }

    public boolean updateBook(Book book) throws SQLException {
        String sql = """
                update books set title = ?, author = ?, total_copies = ?, 
                available_copies = available_copies + (? - total_copies)
                where book_id = ? and available_copies + (?  - total_copies) >= 0
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setInt(3, book.getTotalCopies());
            ps.setInt(4, book.getTotalCopies());
            ps.setInt(5, book.getBookID());
            ps.setInt(6, book.getTotalCopies());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean deleteBook(int id) throws SQLException {
        String sql = """
                delete from books b where b.book_id = ? +
                and not exists(select 1 from transactions t where t.book_id = b.book_id)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    private Book mapBook(ResultSet rs) throws SQLException {
        return new Book(rs.getInt("book_id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("isbn"),
                rs.getInt("total_copies"),
                rs.getInt("available_copies"));
    }
}

