package com.korede.wallet.entity;

import com.korede.wallet.model.enums.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Collection;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity  {

    @Column(unique = true, nullable = false)
    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @Transient // Prevent serialization
    private String password; // Store hashed passwords as a transient field

    @Column(nullable = false)
    @NotNull

    private String hashedPassword;

    @Column(unique = true, nullable = false)
    @NotNull
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;


}