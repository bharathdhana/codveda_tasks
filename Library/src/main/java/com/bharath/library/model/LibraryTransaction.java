package com.bharath.library.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Getter @Setter
public class LibraryTransaction {
    private final int transactionId;
    private final int bookId;
    private final int userId;
    private final String bookTitle;
    private final String userName;
    private final LocalDate borrowDate;
    private final LocalDate returnDate;
    private final String status;

    public LibraryTransaction(int transactionId, int bookId, int userId, String bookTitle, String userName, LocalDate borrowDate, LocalDate returnDate, String status) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.userId = userId;
        this.bookTitle = bookTitle;
        this.userName = userName;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    @Override
    public String toString() {
        return "LibraryTransaction{" +
                "transactionId: " + transactionId +
                ", bookId: " + bookId +
                ", userId: " + userId +
                ", bookTitle: " + bookTitle + '\'' +
                ", userName: " + userName + '\'' +
                ", borrowDate: " + borrowDate +
                ", returnDate: " + returnDate +
                ", status: " + status + '\'' +
                '}';
    }
}
