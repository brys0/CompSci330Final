package org.uwgb.compsci330.server.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String username;

    private String password;
    private int status;

    // Outgoing relationships
    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Relationship> sentRequests = new HashSet<>();

    // Incoming relationships
    @OneToMany(mappedBy = "requestee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Relationship> receivedRequests = new HashSet<>();

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.status = 0;
    }

    public User(String userId) {
        this.id = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<Relationship> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(Set<Relationship> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    public Set<Relationship> getSentRequests() {
        return sentRequests;
    }

    public void setSentRequests(Set<Relationship> sentRequests) {
        this.sentRequests = sentRequests;
    }
}