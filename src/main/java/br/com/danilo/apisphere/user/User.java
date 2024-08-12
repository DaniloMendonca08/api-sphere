package br.com.danilo.apisphere.user;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String bio;
    String email;
    String password;

    //data em que a conta foi criada
    LocalDateTime createdAt;

    //data em que foi atualizado
    LocalDateTime updatedAt;
}