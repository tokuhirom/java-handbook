package com.example;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HogeTest {
    @Test
    public void foo() {
        assertThat(false)
                .isFalse();
    }
}
