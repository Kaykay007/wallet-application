package com.korede.wallet.entity;

import com.korede.wallet.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction extends BaseEntity {

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;
    @Column(unique = true, nullable = false)
    private String transactionReference;


    @Column(nullable = false)
    private BigDecimal sourceAccountBalance;

    @Column(nullable = false)
    private BigDecimal destinationAccountBalance;

}