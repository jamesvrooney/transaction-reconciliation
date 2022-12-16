package com.jamesvrooney.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.format.datetime.DateFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

public class CustomDateValidator implements ConstraintValidator<CustomDateConstraint, String> {

    private DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public void initialize(CustomDateConstraint customDate) {
    }

    @Override
    public boolean isValid(String dateString, ConstraintValidatorContext cxt) {
        dateFormatter.setLenient(false);

        boolean valid = false;

        try {
            dateFormatter.parse(dateString);

            valid = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return valid;
    }
}