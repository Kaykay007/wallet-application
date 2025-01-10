package com.korede.wallet.exception;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpBaseResponse<T extends Serializable> {
    private HttpStatus statusCode;
    private String message;
    private String cause;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    LocalDateTime timestamp;

    private T data;
    List<T> errors;
}