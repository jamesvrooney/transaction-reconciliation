package com.jamesvrooney.model;

import com.jamesvrooney.validation.CustomDateConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashTransaction implements Comparable<CashTransaction> {

    @CustomDateConstraint(message = "Date format: dd/MM/yyyy")
    private String date;

    @NotNull(message = "Transaction type is mandatory")
    private TransactionType type;

    @NotNull(message = "Amount is mandatory")
    private BigDecimal amount;

    @Override
    public int compareTo(CashTransaction o) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            // Sort the CashTransactions in order of date descending
            return dateFormat.parse(o.date).compareTo(dateFormat.parse(this.date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
