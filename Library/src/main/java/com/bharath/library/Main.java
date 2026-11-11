package com.bharath.library;

import com.bharath.library.dao.BookDAO;
import com.bharath.library.dao.UserDAO;
import com.bharath.library.model.Book;
import com.bharath.library.model.User;
import com.bharath.library.service.LibraryService;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final BookDAO bookDAO = new BookDAO();
    private static final UserDAO userDAO = new UserDAO();
    private static final LibraryService service = new LibraryService();

    public static void main(String[] args) throws SQLException {
        System.out.println("--------Library Management System-------");

        while (true) {
            printMenu();
            int choice = readInt("choose an option: ");
            try {
                switch (choice) {
                    case 1 -> addBook();
                    case 2 -> viewBooks();
                    case 3 -> searchBook();
                    case 4 -> updateBook();
                    case 5 -> deleteBook();
                    case 6 -> addUser();
                    case 7 -> viewUsers();
                    case 8 -> searchUser();
                    case 9 -> updateUser();
                    case 10 -> deleteUser();
                    case 11 -> borrowBook();
                    case 12 -> returnBook();
                    case 13 -> viewAllTransactions();
                    case 14 -> viewUserHistory();
                    case 0 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("""
                1.Add book              2.View all books
                3.Search book By ID     4.Update book
                5.Delete book           6.Add user
                7.View all users        8.search user by ID
                9.update user           10.delete user
                11.borrow book          12.return book
                13.view all history     14.view user history
                0.exit
                """);
    }

    private static void addBook() throws SQLException {
        String title  = readText("title: ");
        String author = readText("Author: ");
        String isbn = readText("ISBN: ");
        int copies = readNonNegativeInt("Total copies: ");
        if(copies < 1) throw new IllegalArgumentException("A new book must have at least one copy");
        bookDAO.addBook(new Book(title, author, isbn, copies));
        System.out.println("Book added successfully.");
    }

    private static void viewBooks() throws SQLException {
        var books = bookDAO.getAllBooks();
        if(books.isEmpty()) System.out.println("No books found.");
        else books.forEach(System.out::println);
    }

    private static void searchBook() throws SQLException {
        var book = bookDAO.getBookById(readInt("Book ID: "));
        System.out.println(book == null ? "Book Not Found." : book);
    }

    private static void updateBook() throws SQLException {
        int id = readInt("Book ID to update: ");
        var book = bookDAO.getBookById(id);
        if(book == null) {
            throw new IllegalArgumentException("Book Not Found.");
        }
        book.setTitle(readText("New Title: "));
        book.setAuthor(readText("New Author: "));
        book.setTotalCopies(readNonNegativeInt("New total copies: "));
        System.out.println(bookDAO.updateBook(book) ?
                "Book updated" : "update rejected. total copies cannot be less than borrowed copies");
    }

    private static void deleteBook() throws SQLException {
        int id = readInt("Book ID to delete: ");
        System.out.println(bookDAO.deleteBook(id) ?
                "Book deleted" : "book not found or has transaction history, cannot delete");
    }

    private static void addUser() throws SQLException {
        String name = readText("Name: ");
        String email = readText("Email: ");
        if(!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            throw new IllegalArgumentException("Invalid email address.");
        userDAO.addUser(new User(name, email));
        System.out.println("User added successfully.");
    }

    private static void viewUsers() throws SQLException {
        var users = userDAO.getAllUsers();
        if(users.isEmpty()) System.out.println("No users found.");
        else users.forEach(System.out::println);
    }

    private static void searchUser() throws SQLException {
        var user = userDAO.getUserById(readInt("User ID: "));
        System.out.println(user == null ? "User not found." : user);
    }

    private static void updateUser() throws SQLException {
        int id = readInt("user id to update: ");
        var user = userDAO.getUserById(id);
        if(user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        user.setName(readText("New name: "));
        String email = readText("New Email: ");
        if(!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            throw new IllegalArgumentException("Invalid email address.");
        user.setEmail(email);
        System.out.println(userDAO.updateUser(user) ? "User updated successfully" : "user not found.");
    }

    private static void deleteUser() throws SQLException {
        int id = readInt("user ID to delete: ");
        System.out.println(userDAO.deleteUser(id) ? "User deleted successfully" : "user not found.");
    }

    private static void borrowBook() throws SQLException {
        int userId = readInt("user Id: ");
        int bookId = readInt("Book ID: ");
        int transactionId = service.borrowBook(userId, bookId);
        System.out.println("Book borrowed, transaction ID: " + transactionId);
    }

    private static void returnBook() throws SQLException {
        int transactionId = readInt("transaction ID to return: ");
        System.out.println(service.returnBook(transactionId) ? "Book returned successfully" : "book not found.");
    }

    private static void viewAllTransactions() throws SQLException {
        var rows = service.getAllTransactions();
        if(rows.isEmpty()) System.out.println("No transactions found.");
        else rows.forEach(System.out::println);
    }

    private static void viewUserHistory() throws SQLException {
        int userId = readInt("user ID: ");
        var rows = service.getTransactionsByUser(userId);
        if(rows.isEmpty()) System.out.println("No transactions found for this user.");
        else rows.forEach(System.out::println);
    }

    private static int readInt(String prompt) {
        String value = readText(prompt);
        try { return Integer.parseInt(value); }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Enter a whole number.");
        }
    }

    private static int readNonNegativeInt(String prompt) {
        int value = readInt(prompt);
        if(value < 0) throw new IllegalArgumentException("Enter a non-negative integer.");
        return value;
    }

    private static String readText(String prompt) {
        return readText(prompt, false);
    }

    private static String readText(String prompt, boolean allowBlank) {
        System.out.println(prompt);
        String value = sc.nextLine().trim();
        if(!allowBlank && value.isEmpty())
            throw new IllegalArgumentException("field cannot be empty.");
        return value;
    }
    
}
