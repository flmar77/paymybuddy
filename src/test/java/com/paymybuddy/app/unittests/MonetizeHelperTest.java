package com.paymybuddy.app.unittests;

import com.paymybuddy.app.domain.helper.MonetizeHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MonetizeHelperTest {

    @InjectMocks
    private MonetizeHelper monetizeHelper;

    @Test
    public void should_returnMonetizedAmount_whenGetPositiveMonetizedAmount() {
        assertThat(monetizeHelper.getMonetizedAmount(103)).isEqualTo(0.51f);
    }

    @Test
    public void should_returnMonetizedAmount_whenGetNegativeMonetizedAmount() {
        assertThat(monetizeHelper.getMonetizedAmount(-103)).isEqualTo(0.51f);
    }
}
