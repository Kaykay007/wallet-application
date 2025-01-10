package com.korede.wallet.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.korede.wallet.entity.Account;
import com.korede.wallet.entity.Transaction;
import com.korede.wallet.entity.User;
import com.korede.wallet.exception.WalletException;
import com.korede.wallet.model.enums.AccountStatus;
import com.korede.wallet.model.request.TransferRequest;
import com.korede.wallet.model.response.TransferResponse;
import com.korede.wallet.repository.AccountRepository;
import com.korede.wallet.repository.TransactionRepository;
import com.korede.wallet.repository.UserRepository;
import com.korede.wallet.service.impl.WalletServiceImpl;
import com.korede.wallet.util.PageResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

public class WalletServiceImplTest {

    @InjectMocks
    private WalletServiceImpl walletService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private User user;
    private Account account;
    private Account targetAccount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setStatus(AccountStatus.ACTIVE);

        account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(100));
        account.setUser(user);

        targetAccount = new Account();
        targetAccount.setId(2L);
        targetAccount.setBalance(BigDecimal.valueOf(50));
        targetAccount.setAccountNumber("123456789");
    }

    @Test
    public void testTransfer_HappyPath() {
        TransferRequest request = new TransferRequest();
        request.setAmount(BigDecimal.valueOf(50));
        request.setDestinationAccountNumber("123456789");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.findByUserId(1L)).thenReturn(Collections.singletonList(account));
        when(accountRepository.findByAccountNumber("123456789")).thenReturn(Optional.of(targetAccount));

        TransferResponse response = walletService.transfer(1L, request);

        assertEquals("Transfer successful", response.getMessage());
        assertEquals(BigDecimal.valueOf(50), response.getNewSourceBalance());
        assertEquals(BigDecimal.valueOf(100), response.getNewTargetBalance());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    public void testTransfer_InsufficientBalance() {
        TransferRequest request = new TransferRequest();
        request.setAmount(BigDecimal.valueOf(150));
        request.setDestinationAccountNumber("123456789");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.findByUserId(1L)).thenReturn(Collections.singletonList(account));
        when(accountRepository.findByAccountNumber("123456789")).thenReturn(Optional.of(targetAccount));

        WalletException exception = assertThrows(WalletException.class, () -> {
            walletService.transfer(1L, request);
        });

        assertEquals("Insufficient balance for transfer.", exception.getMessage());
    }


    @Test
    public void testGetAccountBalance_HappyPath() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.findByUserId(1L)).thenReturn(Collections.singletonList(account));

        BigDecimal balance = walletService.getAccountBalance(1L);

        assertEquals(BigDecimal.valueOf(100), balance);
        verify(accountRepository).findByUserId(1L);
    }

    @Test
    public void testGetAccountBalance_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        WalletException exception = assertThrows(WalletException.class, () -> {
            walletService.getAccountBalance(1L);
        });

        assertEquals("User not found.", exception.getMessage());
    }

    @Test
    public void testGetTransactionHistory_HappyPath() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.findByUserId(1L)).thenReturn(Collections.singletonList(account));
        when(transactionRepository.findByAccountId(1L, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0));

        PageResult<Transaction> transactions = walletService.getTransactionHistory(1L, 0, 10);

        assertNotNull(transactions);
        assertEquals(0, transactions.getTotalRecords());
    }

    @Test
    public void testGetTransactionHistory_NoAccountsFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        WalletException exception = assertThrows(WalletException.class, () -> {
            walletService.getTransactionHistory(1L, 0, 10);
        });

        assertEquals("No accounts found for this user.", exception.getMessage());
    }
}