package com.jnu.capstone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    private int userId;

    private String email;
    private String password;
    private String nickname;
    private String campus;
    private String department;
    private int studentNum;
}
