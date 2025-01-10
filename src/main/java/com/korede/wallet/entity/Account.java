package com.korede.wallet.entity;


import com.korede.wallet.model.enums.CurrencyType;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang.RandomStringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")

public class Account  extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected  void prePersist() {
        if (this.accountNumber == null) {
            this.accountNumber = generateAccountNumber();
        }
    }

    private String generateAccountNumber() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        String randomPart = RandomStringUtils.randomNumeric(3) + formattedDateTime;
        return "1" + randomPart;
    }
}