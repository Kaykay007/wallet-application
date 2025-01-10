package com.korede.wallet.controller;

import com.korede.wallet.entity.Transaction;
import com.korede.wallet.exception.HttpBaseResponse;
import com.korede.wallet.model.request.DepositRequest;
import com.korede.wallet.model.request.TransferRequest;
import com.korede.wallet.model.request.WithdrawRequest;
import com.korede.wallet.model.response.DepositResponse;
import com.korede.wallet.model.response.TransferResponse;
import com.korede.wallet.model.response.WithdrawResponse;
import com.korede.wallet.service.WalletService;
import com.korede.wallet.util.PageResult;
import com.korede.wallet.util.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
@Slf4j
public class WalletController {

    private final WalletService walletService;
    private final TokenUtil tokenUtil;

//Todo - User id are temporarily hardcoded for testing.  to use authenticated request see todo below

    @Operation(summary = "Deposit funds into a wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpBaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/deposit")
    public ResponseEntity<HttpBaseResponse<DepositResponse>> deposit(
            @Valid @RequestBody DepositRequest request) {
        log.info("Deposit request body: {}", request);

        Long userId = 3L;
        DepositResponse depositResponse = walletService.deposit(userId, request);

        log.info("Deposit processed: {}", depositResponse);

        HttpBaseResponse<DepositResponse> response = HttpBaseResponse.<DepositResponse>builder()
                .statusCode(HttpStatus.OK)
                .message("Deposit successful")
                .data(depositResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Withdraw funds from a wallet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdrawal successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpBaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/withdraw")
    public ResponseEntity<HttpBaseResponse<WithdrawResponse>> withdraw(
            @Valid @RequestBody WithdrawRequest request) {
        log.info("Withdraw request body: {}", request);

        Long userId = 3L;
        WithdrawResponse withdrawResponse = walletService.withdraw(userId, request);

        log.info("Withdrawal processed: {}", withdrawResponse);

        HttpBaseResponse<WithdrawResponse> response = HttpBaseResponse.<WithdrawResponse>builder()
                .statusCode(HttpStatus.OK)
                .message("Withdrawal successful")
                .data(withdrawResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Transfer funds between wallets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpBaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/transfer")
    public ResponseEntity<HttpBaseResponse<TransferResponse>> transfer(
            @Valid @RequestBody TransferRequest request) {
        log.info("Transfer request body: {}", request);

        Long userId = 3L;
        TransferResponse transferResponse = walletService.transfer(userId, request);

        log.info("Transfer processed: {}", transferResponse);

        HttpBaseResponse<TransferResponse> response = HttpBaseResponse.<TransferResponse>builder()
                .statusCode(HttpStatus.OK)
                .message("Transfer successful")
                .data(transferResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retrieve wallet balance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpBaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/balance")
    public ResponseEntity<HttpBaseResponse<BigDecimal>> getBalance(
            @Parameter(description = "ID of the user", required = true) @RequestParam Long userId) {
        BigDecimal balance = walletService.getAccountBalance(userId);
        HttpBaseResponse<BigDecimal> response = new HttpBaseResponse<>();
        response.setData(balance);
        response.setMessage("Balance retrieved successfully");
        response.setStatusCode(HttpStatus.OK);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retrieve transaction history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction history retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpBaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/transactions")
    public ResponseEntity<HttpBaseResponse<PageResult<Transaction>>> getTransactionHistory(
            @Parameter(description = "ID of the user", required = true) @RequestParam Long userId,
            @Parameter(description = "Page number", required = false, example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", required = false, example = "10") @RequestParam(defaultValue = "10") int size) {
        PageResult<Transaction> transactions = walletService.getTransactionHistory(userId, page, size);
        HttpBaseResponse<PageResult<Transaction>> response = new HttpBaseResponse<>();
        response.setData(transactions);
        response.setMessage("Transaction history retrieved successfully");
        response.setStatusCode(HttpStatus.OK);

        return ResponseEntity.ok(response);
    }




//Todo- A valid secret key is needed to use an authenticated req for the user with the below methods.

//    @PostMapping("/deposit")
//    public ResponseEntity<HttpBaseResponse<DepositResponse>> deposit(
//            @RequestHeader("Authorization") String token,
//            @Valid @RequestBody DepositRequest request) {
//        log.info("Deposit request received for token: {}", token);
//        log.info("Deposit request body: {}", request);
//
//        DepositResponse depositResponse = walletService.deposit(getUserIdFromToken(token), request);
//
//        log.info("Deposit processed: {}", depositResponse);
//
//        HttpBaseResponse<DepositResponse> response = HttpBaseResponse.<DepositResponse>builder()
//                .statusCode(HttpStatus.OK)
//                .message("Deposit successful")
//                .data(depositResponse)
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/withdraw")
//    public ResponseEntity<HttpBaseResponse<WithdrawResponse>> withdraw(
//            @RequestHeader("Authorization") String token,
//            @Valid @RequestBody WithdrawRequest request) {
//
//        log.info("Withdraw request received for token: {}", token);
//        log.info("Withdraw request body: {}", request);
//
//        WithdrawResponse withdrawResponse = walletService.withdraw(getUserIdFromToken(token), request);
//
//        log.info("Withdrawal processed: {}", withdrawResponse);
//
//        HttpBaseResponse<WithdrawResponse> response = HttpBaseResponse.<WithdrawResponse>builder()
//                .statusCode(HttpStatus.OK)
//                .message("Withdrawal successful")
//                .data(withdrawResponse)
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/transfer")
//    public ResponseEntity<HttpBaseResponse<TransferResponse>> transfer(
//            @RequestHeader("Authorization") String token,
//            @Valid @RequestBody TransferRequest request) {
//
//        log.info("Transfer request body: {}", request);
//
//        TransferResponse transferResponse = walletService.transfer(getUserIdFromToken(token), request);
//
//        log.info("Transfer processed: {}", transferResponse);
//
//        HttpBaseResponse<TransferResponse> response = HttpBaseResponse.<TransferResponse>builder()
//                .statusCode(HttpStatus.OK)
//                .message("Transfer successful")
//                .data(transferResponse)
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        return ResponseEntity.ok(response);
//    }


 private Long getUserIdFromToken(String token) {
    if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);
    }
    return tokenUtil.getUserIdFromToken(token);
}

}