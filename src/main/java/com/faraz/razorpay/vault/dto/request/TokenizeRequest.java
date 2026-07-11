package com.faraz.razorpay.vault.dto.request;

import com.faraz.razorpay.vault.validation.ExpiryYear;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.LuhnCheck;

import java.util.UUID;

public record TokenizeRequest(

        @NotBlank(message = "Pan is required")
        @LuhnCheck(message = "Invalid pan number")
        @Pattern(regexp = "^[0-9]{13,19}$", message = "Invalid pan number")
        String pan,

//        @NotBlank(message = "PAN is required")
//        @Pattern(
//                regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$",
//                message = "Invalid PAN number format"
//        )
//        String pan;


        @NotBlank(message = "CVV is required")
        @Pattern(regexp = "^[0-9]{3,4}$", message = "CVV length is invalid")
        String cvv,

        @NotNull(message = "Expiry month is required")
        @Min(value = 1, message = "Invalid expiry month")
        @Max(value = 12, message = "Invalid expiry month")
        Integer expiryMonth,

        @NotNull(message = "Expiry year is required")
        @ExpiryYear
        Integer expiryYear,

        UUID customerId,

        @Size(min = 3, message = "Cardholder name must be at least 3 characters long")
        String cardHolderName

) {
}
