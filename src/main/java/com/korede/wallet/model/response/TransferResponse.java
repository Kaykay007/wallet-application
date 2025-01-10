package com.korede.wallet.model.response;

import com.korede.wallet.model.enums.TransferStatus;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse implements Serializable {
    private TransferStatus status;
    private String message;
    private BigDecimal newSourceBalance;
    private BigDecimal newTargetBalance;
    private String transactionReference;
}