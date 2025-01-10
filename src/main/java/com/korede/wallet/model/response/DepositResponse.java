package com.korede.wallet.model.response;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositResponse implements Serializable {
    private String message;
    private BigDecimal newBalance;
    private String transactionReference;
}