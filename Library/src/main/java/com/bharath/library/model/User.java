package com.bharath.library.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
public class User {
    private int userId;
    private String name;
    private String email;
    private LocalDateTime createdAt;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId: " + userId +
                ", name: " + name + '\'' +
                ", email: " + email + '\'' +
                ", Joined: " + createdAt +
                '}';
    }
}
