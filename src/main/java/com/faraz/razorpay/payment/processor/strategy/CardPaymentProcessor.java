package com.faraz.razorpay.payment.processor.strategy;

import com.faraz.razorpay.common.util.RandomizerUtil;
import com.faraz.razorpay.payment.processor.PaymentProcessor;
import com.faraz.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.faraz.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CardPaymentProcessor implements PaymentProcessor {

    public static final String PAN_CARD_DECLINED = "400000000000002";
    public static final String PAN_CARD_EXPIRED = "400000000000069";

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        String pan = request.pan();

        if (PAN_CARD_DECLINED.equals(pan)) {
            log.warn("Card declined for PAN: {}", pan);
            return new PaymentProcessorResponse.Failure("CARD_DECLINED", "The card was declined.");
        }

        if (PAN_CARD_EXPIRED.equals(pan)) {
            log.warn("Card expired for PAN: {}", pan);
            return new PaymentProcessorResponse.Failure("CARD_EXPIRED", "The card has expired.");
        }

        String processorRef = "CARD_PROCESSOR"+ RandomizerUtil.randomBase64(16);

        return new PaymentProcessorResponse.Pending(processorRef);
    }
}
