package com.jnu.capstone.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users") //데이터베이스 users 테이블과 매핑
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;


    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100, name = "password")
    private String password;

    @Column(nullable = false, length = 20)
    private String nickname;

    @ManyToOne
    @JoinColumn(name = "campus_id", referencedColumnName = "campus_id", nullable = false)
    private School campus;

    @Column(nullable = true, length = 100)
    private String department;

    @Column(nullable = false)
    private int studentNum;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int goodCount = 0;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int badCount = 0;

//    @Column(nullable = true, length = 255)
//    private String profileImageUrl;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean emailVerified = false;  // 이메일 인증 여부

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public School getCampus() { return campus; }
    public void setCampus(School campus) { this.campus = campus; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public int getStudentNum() { return studentNum; }
    public void setStudentNum(int studentNum) { this.studentNum = studentNum; }
    public int getGoodCount() { return goodCount; }
    public void setGoodCount(int goodCount) { this.goodCount = goodCount; }
    public int getBadCount() { return badCount; }
    public void setBadCount(int badCount) { this.badCount = badCount; }
    public List<Post> getPosts() { return posts; }
    public void setPosts(List<Post> posts) { this.posts = posts; }
//    public String getProfileImageUrl() { return profileImageUrl; }
//    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }
}


//package com.jnu.capstone.entity;
//
//import jakarta.persistence.*;
//import java.util.List;
//
//@Entity
//@Table(name = "users") //데이터베이스 users 테이블과 매핑
//public class User {
//    @Id //기본키 필드
//    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동으로 증가하는 기본키를 생성
//    @Column(name = "user_id")
//    private int userId;
//
//    @Column(nullable = false, length = 100) //NOTNULL,최대100자
//    private String email;
//
//    @Column(nullable = false, length = 100)
//    private String password;
//
//    @Column(nullable = false, length = 20)
//    private String nickname;
//
//    //관계 설정 (다대일 관계)
//    @ManyToOne //다수의 사용자(User)가 하나의 캠퍼스(School)에 속하는 구조
//    @JoinColumn(name = "campus_id", referencedColumnName = "campus_id", nullable = false)
//    private School campus;
//
//    @Column(nullable = true, length = 100)
//    private String department;
//
//    @Column(nullable = false)
//    private int studentNum;
//
//    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
//    private int goodCount = 0;
//
//    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
//    private int badCount = 0;
//
//    @Column(nullable = true, length = 255)
//    private String profileImageUrl;  // 추가된 필드
//
//    //하나의 사용자(User)은 여러 개의 게시물(Post)를 가질 수 있음
//    //cascade = CascadeType.ALL :: User가 삭제될 때 관련 Post도 함께 삭제
//    //orphanRemoval = true :: User가 Post와의 관계를 제거할 때 고아가 된 Post도 삭제
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Post> posts;
//
//    // Getters and Setters
//    public int getUserId() { return userId; }
//    public void setUserId(int userId) { this.userId = userId; }
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//    public String getPassword() { return password; }
//    public void setPassword(String password) { this.password = password; }
//    public String getNickname() { return nickname; }
//    public void setNickname(String nickname) { this.nickname = nickname; }
//    public School getCampus() { return campus; }
//    public void setCampus(School campus) { this.campus = campus; }
//    public String getDepartment() { return department; }
//    public void setDepartment(String department) { this.department = department; }
//    public int getStudentNum() { return studentNum; }
//    public void setStudentNum(int studentNum) { this.studentNum = studentNum; }
//    public int getGoodCount() { return goodCount; }
//    public void setGoodCount(int goodCount) { this.goodCount = goodCount; }
//    public int getBadCount() { return badCount; }
//    public void setBadCount(int badCount) { this.badCount = badCount; }
//    public List<Post> getPosts() { return posts; }
//    public void setPosts(List<Post> posts) { this.posts = posts; }
//    public String getProfileImageUrl() { return profileImageUrl; }
//    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
//}