package org.uwgb.compsci330.server.entity.conversation;

import jakarta.persistence.*;
import lombok.Getter;
import org.uwgb.compsci330.server.entity.user.User;

import java.util.Set;

@Entity(name = "conversations")
@Table(
        name = "conversations"
)
public class Conversation {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Getter
    @ManyToMany
    @JoinTable(
            name = "conversation_participants",
            joinColumns = @JoinColumn(name = "conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participants;


    public Conversation() {}

    public Conversation(Set<User> users) {
        this.participants = users;
    }

}
