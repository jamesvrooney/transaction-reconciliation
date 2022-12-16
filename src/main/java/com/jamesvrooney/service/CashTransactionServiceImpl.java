package com.jamesvrooney.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jamesvrooney.model.CashTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class CashTransactionServiceImpl implements CashTransactionService {

//    @Value("classpath:static/cash-transactions.json")
    @Value("${cash.transaction.file}")
    private String resourceFile;
//    private Resource resourceFile;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<CashTransaction> save(List<CashTransaction> cashTransactions) {
        List<CashTransaction> updatedCashTransactions = cashTransactions.stream().sorted().toList();

        // Get list of existing transactions from file
        List<CashTransaction> existingCashTransactions = getExistingTransactions();

        if (!existingCashTransactions.isEmpty()) {
            // Check new transaction against existing transactions,
            // - if they don't exist add new transaction
            // - if they do exist with the same type add
            var cashTransactionsByTypeAndDate = existingCashTransactions.stream()
                    .collect(groupingBy(CashTransaction::getType, groupingBy(CashTransaction::getDate)));

            for (CashTransaction cashTransaction: cashTransactions) {
                var transactionType = cashTransaction.getType();
                var transactionDate = cashTransaction.getDate();

                // If a value already exists for the same date
                if (cashTransactionsByTypeAndDate.get(transactionType).containsKey(transactionDate)) {
                    // Update the existing value
                    CashTransaction existingTransaction = cashTransactionsByTypeAndDate.get(transactionType).get(transactionDate).get(0);

                    var existingTransactionValue = existingTransaction.getAmount();
                    var updatedTransactionValue = existingTransactionValue.add(cashTransaction.getAmount());

                    existingTransaction.setAmount(updatedTransactionValue);

                    cashTransactionsByTypeAndDate.get(transactionType).get(transactionDate).clear();
                    cashTransactionsByTypeAndDate.get(transactionType).get(transactionDate).add(0, existingTransaction);

                    log.info("Handling duplicate cash transaction.");
                } else {
                    cashTransactionsByTypeAndDate.get(transactionType).put(transactionDate, Arrays.asList(cashTransaction));
                }
            }

            // Convert updated Map back to List
            updatedCashTransactions = cashTransactionsByTypeAndDate.values()
                    .stream()
                    .map(g -> g.values())
                    .flatMap(Collection::stream)
                    .map(g -> g.get(0))
                    .sorted()
                    .collect(toList());

            // Save updated list of cash transactions to file
            saveListToFile(updatedCashTransactions);

            return updatedCashTransactions;
        } else {
            saveListToFile(updatedCashTransactions);

            return updatedCashTransactions;
        }
    }

    @Override
    public List<CashTransaction> getAll() {
        List<CashTransaction> cashTransactions = getExistingTransactions();

        return cashTransactions;
    }

    private void saveListToFile(List<CashTransaction> cashTransactions) {
        try {
            ObjectWriter writer = new ObjectMapper().writer(new DefaultPrettyPrinter());

            File file = ResourceUtils.getFile(resourceFile);

            writer.writeValue(file, cashTransactions);
//            writer.writeValue(resourceFile.getFile(), cashTransactions);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<CashTransaction> getExistingTransactions() {
        List<CashTransaction> existingCashTransactions = new ArrayList<>();

        try {
            File file = ResourceUtils.getFile(resourceFile);

            existingCashTransactions = mapper.readValue(file, new TypeReference<>(){});
//            existingCashTransactions = mapper.readValue(resourceFile.getFile(), new TypeReference<>(){});
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return existingCashTransactions;
    }
}
