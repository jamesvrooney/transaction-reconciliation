package com.jamesvrooney.service;

import com.jamesvrooney.model.CashTransaction;

import java.util.List;

public interface CashTransactionService {
    List<CashTransaction> save(List<CashTransaction> cashTransactions);
    List<CashTransaction> getAll();
}
