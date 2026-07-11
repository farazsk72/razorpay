package com.faraz.razorpay.vault.validation;

import jakarta.validation.ConstraintValidator;

public class ExpiryYearValidator implements ConstraintValidator<ExpiryYear, Integer> {

    @Override
    public boolean isValid(Integer inputYear, jakarta.validation.ConstraintValidatorContext context) {

        if (inputYear == null) {
            return false;
        }

        int currentYear = java.time.Year.now().getValue();
        return inputYear >= currentYear;



    }


}
