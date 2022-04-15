package com.paymybuddy.app.domain.helper;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class MonetizeHelper {

    public static final float MONETIZED_PERCENT = 0.005F;

    public float getMonetizedAmount(float amount) {

        BigDecimal monetizedPercentBd = BigDecimal.valueOf(MONETIZED_PERCENT);
        BigDecimal amountBd = BigDecimal.valueOf(amount).abs();
        BigDecimal monetizedAmountBdRaw = monetizedPercentBd.multiply(amountBd);
        BigDecimal monetizedAmountBdRounded = monetizedAmountBdRaw.setScale(2, RoundingMode.HALF_UP);

        return monetizedAmountBdRounded.floatValue();
    }
}
