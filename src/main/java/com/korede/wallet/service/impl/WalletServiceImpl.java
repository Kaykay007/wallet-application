package com.korede.wallet.service.impl;

import com.korede.wallet.entity.Account;
import com.korede.wallet.entity.Transaction;
import com.korede.wallet.entity.User;
import com.korede.wallet.exception.WalletException;
import com.korede.wallet.model.enums.AccountStatus;
import com.korede.wallet.model.enums.TransactionType;
import com.korede.wallet.model.request.DepositRequest;
import com.korede.wallet.model.request.TransferRequest;
import com.korede.wallet.model.request.WithdrawRequest;
import com.korede.wallet.model.response.DepositResponse;
import com.korede.wallet.model.response.TransferResponse;
import com.korede.wallet.model.response.WithdrawResponse;
import com.korede.wallet.repository.AccountRepository;
import com.korede.wallet.repository.TransactionRepository;
import com.korede.wallet.repository.UserRepository;
import com.korede.wallet.service.WalletService;
import com.korede.wallet.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class WalletServiceImpl implements WalletService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

@Override
@Transactional
public DepositResponse deposit(Long userId, DepositRequest request) {
    try {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new WalletException("User not found."));
        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new WalletException("User account is not active.");
        }
        List<Account> accounts = accountRepository.findByUserId(userId);

        if (accounts.isEmpty()) {
            throw new WalletException("No accounts found for this user.");
        }

        Account account = accounts.stream()
                .findFirst()
                .orElseThrow(() -> new WalletException("No accounts found for this user."));

        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .timestamp(LocalDateTime.now())
                .type(TransactionType.DEPOSIT)
                .account(account)
                .transactionReference(generateTransactionReference())
                .sourceAccountBalance(account.getBalance())
                .destinationAccountBalance(account.getBalance())
                .build();
        transactionRepository.save(transaction);

        return DepositResponse.builder()
                .message("Deposit successful")
                .newBalance(account.getBalance())
                .transactionReference(transaction.getTransactionReference())
                .build();

    } catch (WalletException e) {
        return DepositResponse.builder()
                .message(e.getMessage())
                .newBalance(null)
                .transactionReference(null)
                .build();
    } catch (Exception e) {

        return DepositResponse.builder()
                .message("An unexpected error occurred: " + e.getMessage())
                .newBalance(null)
                .transactionReference(null)
                .build();
    }
}


    @Override
    @Transactional
    public WithdrawResponse withdraw(Long userId, WithdrawRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new WalletException("User not found."));
        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new WalletException("User account is not active.");
        }

        List<Account> accounts = accountRepository.findByUserId(userId);
        if (accounts.isEmpty()) {
            throw new WalletException("No accounts found for this user.");
        }

        Account account = accounts.stream()
                .findFirst()
                .orElseThrow(() -> new WalletException("No accounts found for this user."));

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new WalletException("Insufficient balance.");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));

        String transactionReference = generateTransactionReference();
        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .timestamp(LocalDateTime.now())
                .type(TransactionType.WITHDRAWAL)
                .account(account)
                .transactionReference(transactionReference)
                .sourceAccountBalance(account.getBalance())
                .destinationAccountBalance(account.getBalance())
                .build();

        try {
            accountRepository.save(account);
            transactionRepository.save(transaction);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to save transaction due to duplicate reference: {}", transactionReference);
            throw new WalletException("Transaction could not be processed. Please try again.");
        }

        return WithdrawResponse.builder()
                .message("Withdrawal successful")
                .newBalance(account.getBalance())
                .transactionReference(transactionReference)
                .build();
    }

    @Override
    @Transactional
    public TransferResponse transfer(Long userId, TransferRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new WalletException("User not found."));

        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new WalletException("User account is not active.");
        }

        List<Account> accounts = accountRepository.findByUserId(userId);
        if (accounts.isEmpty()) {
            throw new WalletException("No accounts found for this user.");
        }

        Account sourceAccount = accounts.stream()
                .findFirst()
                .orElseThrow(() -> new WalletException("No accounts found for this user."));

        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new WalletException("Insufficient balance for transfer.");
        }

        Account targetAccount = accountRepository.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(() -> new WalletException("Target account not found."));


        String transactionReference = generateTransactionReference();

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        accountRepository.save(sourceAccount);

        targetAccount.setBalance(targetAccount.getBalance().add(request.getAmount()));
        accountRepository.save(targetAccount);

        Transaction withdrawalTransaction = Transaction.builder()
                .amount(request.getAmount())
                .timestamp(LocalDateTime.now())
                .type(TransactionType.TRANSFER_OUT)
                .account(sourceAccount)
                .destinationAccount(targetAccount)
                .transactionReference(transactionReference)
                .sourceAccountBalance(sourceAccount.getBalance())
                .destinationAccountBalance(targetAccount.getBalance())
                .build();

        try {
            transactionRepository.save(withdrawalTransaction);
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to save transaction due to duplicate reference: {}", transactionReference);
            throw new WalletException("Transaction could not be processed. Please try again.");
        }

        return TransferResponse.builder()
                .message("Transfer successful")
                .newSourceBalance(sourceAccount.getBalance())
                .newTargetBalance(targetAccount.getBalance())
                .transactionReference(transactionReference)
                .build();
    }
@Override
    public BigDecimal getAccountBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new WalletException("User not found."));

        List<Account> accounts = accountRepository.findByUserId(userId);
        if (accounts.isEmpty()) {
            throw new WalletException("No accounts found for this user.");
        }

        Account account = accounts.stream()
                .findFirst()
                .orElseThrow(() -> new WalletException("No accounts found for this user."));

        return account.getBalance();
    }

@Override
    public PageResult<Transaction> getTransactionHistory(Long userId, int page, int size) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new WalletException("User not found."));

        List<Account> accounts = accountRepository.findByUserId(userId);
        if (accounts.isEmpty()) {
            throw new WalletException("No accounts found for this user.");
        }
        Account account = accounts.stream()
                .findFirst()
                .orElseThrow(() -> new WalletException("No accounts found for this user."));

        Page<Transaction> transactionPage = transactionRepository.findByAccountId(account.getId(), PageRequest.of(page, size));

        return new PageResult<>(
                transactionPage.getContent(),
                transactionPage.getTotalElements(),
                transactionPage.getTotalPages(),
                transactionPage.getNumber()
        );
    }

//    private BigDecimal convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
//
//        BigDecimal conversionRate = getConversionRate(fromCurrency, toCurrency);
//        return amount.multiply(conversionRate);
//    }

//    private BigDecimal getConversionRate(String fromCurrency, String toCurrency) {
//        return BigDecimal.valueOf(1.5);
//    }


    private String generateTransactionReference() {
        return "TRANS-" + UUID.randomUUID().toString();
    }

}
