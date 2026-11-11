package com.bharath.library.service;

import com.bharath.library.config.DBConnection;
import com.bharath.library.model.LibraryTransaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryService {
    public int borrowBook(int userId, int bookId) throws SQLException {
        String reduceStock = """
                update books set available_copies = available_copies - 1
                where book_id = ? and available_copies > 0
                """;
        String insertTransaction = """
                insert into transactions(book_id, user_id, status) 
                values (?, ?, 'BORROWED')
                """;

        try(Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try{
                try(PreparedStatement ps = con.prepareStatement(reduceStock)) {
                    ps.setInt(1, bookId);
                    if(ps.executeUpdate() != 1) {
                        throw new SQLException("Books does not exist or no copies are available");
                    }
                }

                int transactionId;
                try(PreparedStatement ps = con.prepareStatement(insertTransaction,
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, bookId);
                    ps.setInt(2, userId);
                    ps.executeUpdate();
                    try(ResultSet keys = ps.getGeneratedKeys()) {
                        if(!keys.next()) throw new SQLException("Could not create borrow record");
                        transactionId = keys.getInt(1);
                    }
                }

                con.commit();
                return transactionId;
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        }
    }

    public boolean returnBook(int transactionId) throws SQLException {
        String findActive = """
                select book_id from transactions where transaction_id = ?
                and status = 'BORROWED'
                for update
                """;
        String markReturned = """
                update transactions set status = 'RETURNED', return_date = CURRENT_DATE
                where transaction_id = ? and status = 'BORROWED'
                """;
        String addStock = """
                update books set available_copies = available_copies + 1
                where book_id = ?
                """;

        try(Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                int bookId;
                try(PreparedStatement ps = con.prepareStatement(findActive)) {
                    ps.setInt(1, transactionId);
                    try(ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return false;
                        }
                        bookId = rs.getInt("book_id");
                    }
                }

                try(PreparedStatement ps = con.prepareStatement(markReturned)) {
                    ps.setInt(1, transactionId);
                    if(ps.executeUpdate() != 1) {
                        throw new SQLException("could not update the borrow record");
                    }
                }

                try(PreparedStatement ps = con.prepareStatement(addStock)) {
                    ps.setInt(1, bookId);
                    if(ps.executeUpdate() != 1) {
                        throw new SQLException("book record was not found while returning");
                    }
                }

                con.commit();
                return true;
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        }
    }

    public List<LibraryTransaction> getAllTransactions() throws SQLException {
        String sql = """
                select t.transaction_id, t.book_id, b.title as book_title,
                t.user_id, u.name as user_name, t.borrow_date, t.return_date, t.status
                from transactions t 
                join books b on b.book_id = t.book_id
                join users u on u.user_id = t.user_id
                order by t.transaction_id desc
                """;

        List<LibraryTransaction> result = new ArrayList<>();
        try(Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new LibraryTransaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("book_id"),
                        rs.getInt("user_id"),
                        rs.getString("book_title"),
                        rs.getString("user_name"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("return_date") == null ? null : rs.getDate("return_date").toLocalDate(),
                        rs.getString("status")));
            }
        }
        return result;
    }

    public List<LibraryTransaction> getTransactionsByUser(int userId) throws SQLException {
        String sql = """
                select t.transaction_id, t.book_id, b.title AS book_title,
                t.user_id, u.name AS user_name, t.borrow_date, t.return_date, t.status
                from transactions t
                join books b ON b.book_id = t.book_id
                join users u ON u.user_id = t.user_id
                where t.user_id = ?
                order BY t.transaction_id desc
                """;

        List<LibraryTransaction> result = new ArrayList<>();
        try(Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new LibraryTransaction(
                            rs.getInt("transaction_id"),
                            rs.getInt("book_id"),
                            rs.getInt("user_id"),
                            rs.getString("book_title"),
                            rs.getString("user_name"),
                            rs.getDate("borrow_date").toLocalDate(),
                            rs.getDate("return_date") == null ? null : rs.getDate("return_date").toLocalDate(),
                            rs.getString("status")));
                }
            }
        }
        return result;
    }

}
