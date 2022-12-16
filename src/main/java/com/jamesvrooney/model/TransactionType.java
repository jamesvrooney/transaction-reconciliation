package com.jamesvrooney.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TransactionType {
    @JsonProperty("credit")
    CREDIT("credit"),
    @JsonProperty("debit")
    DEBIT("debit");

    private String transactionType;

    TransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
