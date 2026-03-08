package org.uwgb.compsci330.server.entity.user;

import jakarta.persistence.*;
import org.uwgb.compsci330.server.entity.relationship.Relationship;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String username;

    private String password;
    @Enumerated(EnumType.ORDINAL)
    private UserStatus status;

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
        this.status = UserStatus.OFFLINE;
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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
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