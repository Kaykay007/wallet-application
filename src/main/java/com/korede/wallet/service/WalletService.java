package com.korede.wallet.service;

import com.korede.wallet.entity.Transaction;
import com.korede.wallet.model.request.DepositRequest;
import com.korede.wallet.model.request.TransferRequest;
import com.korede.wallet.model.request.WithdrawRequest;
import com.korede.wallet.model.response.DepositResponse;
import com.korede.wallet.model.response.TransferResponse;
import com.korede.wallet.model.response.WithdrawResponse;
import com.korede.wallet.util.PageResult;

import java.math.BigDecimal;

public interface WalletService {
    DepositResponse deposit(Long userId, DepositRequest request);
     TransferResponse transfer(Long userId, TransferRequest request);
    WithdrawResponse withdraw(Long userId, WithdrawRequest request);
    BigDecimal getAccountBalance(Long userId);
    PageResult<Transaction> getTransactionHistory(Long userId, int page, int size);

}