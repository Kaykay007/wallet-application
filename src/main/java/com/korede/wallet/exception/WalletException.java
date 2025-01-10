package com.korede.wallet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;
@Getter
public class WalletException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final HttpStatus status;

    public WalletException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public WalletException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

}
