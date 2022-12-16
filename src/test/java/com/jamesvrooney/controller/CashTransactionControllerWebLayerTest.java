package com.jamesvrooney.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamesvrooney.model.CashTransaction;
import com.jamesvrooney.model.TransactionType;
import com.jamesvrooney.service.CashTransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CashTransactionController.class)
class CashTransactionControllerWebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CashTransactionService cashTransactionService;

    @Test
    @DisplayName("Get all existing cash transactions")
    void testGetCashTransactions() throws Exception {
        // Arrange
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/transactions/all")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        List<CashTransaction> existingCashTransactions = getExistingCashTransactions();

        when(cashTransactionService.getAll()).thenReturn(existingCashTransactions);

        // Act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String cashTransactionsAsString = result.getResponse().getContentAsString();

        List<CashTransaction> cashTransactions =
                new ObjectMapper().readValue(cashTransactionsAsString, new TypeReference<>(){});

        // Assert
        assertEquals(3, cashTransactions.size());
    }

    @DisplayName("Try to save cash transactions with invalid date")
    @ParameterizedTest
    @CsvFileSource(resources = "/invalidDates.csv")
    void testSaveCashTransactionWithInvalidDates(String date) throws Exception {
        // Arrange
        List<CashTransaction> cashTransactionsWithInvalidDate = getCashTransaction();
        cashTransactionsWithInvalidDate.get(0).setDate(date);

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/transactions/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(cashTransactionsWithInvalidDate));

        // Act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @DisplayName("Try to save cash transactions with valid dates")
    @ParameterizedTest
    @CsvFileSource(resources = "/validDates.csv")
    void testSaveCashTransactionWithValidDates(String date) throws Exception {
        // Arrange
        List<CashTransaction> cashTransactionsWithInvalidDate = getCashTransaction();
        cashTransactionsWithInvalidDate.get(0).setDate(date);

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/transactions/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(cashTransactionsWithInvalidDate));

        // Act
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        // Assert
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    private List<CashTransaction> getCashTransaction() {
        // NB: Date format: dd-MM-yyyy

        List<CashTransaction> cashTransactions = Arrays.asList(
                new CashTransaction("10-12-2018", TransactionType.CREDIT, BigDecimal.valueOf(67.20))
        );

        return cashTransactions;
    }


    private List<CashTransaction> getExistingCashTransactions() {
        List<CashTransaction> cashTransactions = Arrays.asList(
                new CashTransaction("10-12-2020", TransactionType.CREDIT, BigDecimal.valueOf(23.45)),
                new CashTransaction("10-12-2019", TransactionType.CREDIT, BigDecimal.valueOf(45.10)),
                new CashTransaction("10-12-2018", TransactionType.CREDIT, BigDecimal.valueOf(67.20))
        );

        return cashTransactions;
    }
}