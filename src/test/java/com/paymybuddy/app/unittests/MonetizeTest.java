package com.paymybuddy.app.unittests;

import com.paymybuddy.app.domain.helper.Monetize;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MonetizeTest {

    @InjectMocks
    private Monetize monetize;

    @Test
    public void should_returnMonetizedAmount_whenGetMonetizedAmount() {
        assertThat(monetize.getMonetizedAmount(103)).isEqualTo(0.51f);
    }

}
