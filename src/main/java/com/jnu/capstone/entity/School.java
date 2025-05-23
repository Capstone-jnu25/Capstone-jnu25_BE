package com.jnu.capstone.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "school")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campus_id")
    private int campusId;

    @Column(name = "campus_name", nullable = false, length = 100)
    private String campusName;

    @Column(nullable = false, columnDefinition = "DECIMAL(10,8)")
    private double latitude;

    @Column(nullable = false, columnDefinition = "DECIMAL(11,8)")
    private double longitude;

    // Post와의 관계 설정
    @OneToMany(mappedBy = "campus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    // User와의 관계 설정
    @OneToMany(mappedBy = "campus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users;

    // Getters and Setters
    public int getCampusId() { return campusId; }
    public void setCampusId(int campusId) { this.campusId = campusId; }
    public String getCampusName() { return campusName; }
    public void setCampusName(String campusName) { this.campusName = campusName; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public List<Post> getPosts() { return posts; }
    public void setPosts(List<Post> posts) { this.posts = posts; }
    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }
}
