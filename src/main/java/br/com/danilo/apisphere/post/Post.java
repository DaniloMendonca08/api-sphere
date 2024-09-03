package br.com.danilo.apisphere.post;

import br.com.danilo.apisphere.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Data //annotation para criação dos getters e setters automaticamente
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String text;
    LocalDateTime createdAt;

    @ManyToOne
    User user;
}
