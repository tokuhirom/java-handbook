package com.example.mockito;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

interface Gah {
    void bar();
}

public class MockitoDoReturnTest {
    @Test
    public void test() {
        Gah mock = mock(Gah.class);

        doThrow(IllegalArgumentException.class).when(mock).bar();

        assertThatThrownBy(mock::bar)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
