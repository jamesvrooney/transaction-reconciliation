package com.jamesvrooney.controller;

import com.jamesvrooney.model.CashTransaction;
import com.jamesvrooney.service.CashTransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("api/transactions")
public class CashTransactionController {

    private final CashTransactionService cashTransactionService;

    @GetMapping("all")
    public ResponseEntity<List<CashTransaction>> getCashTransactions() {
        List<CashTransaction> cashTransactions = cashTransactionService.getAll();

        return new ResponseEntity<>(cashTransactions, HttpStatus.OK);
    }

    @PostMapping("add")
    public List<CashTransaction> processNewTransactions(@Valid @RequestBody List<CashTransaction> newTransactions) {
        // TODO: Validate the list of CRM before further processing
        var updatedTransactionsList = cashTransactionService.save(newTransactions);

        return updatedTransactionsList;
    }
}
