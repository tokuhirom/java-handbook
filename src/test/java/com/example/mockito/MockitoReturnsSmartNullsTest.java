package com.example.mockito;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.SmartNullPointerException;

import static org.mockito.Mockito.mock;

interface Bar {
    void hoge();
}

interface Foo {
    Bar boz();
}

public class MockitoReturnsSmartNullsTest {
    @Test
    public void test() {
        Foo mock = mock(Foo.class, Mockito.RETURNS_SMART_NULLS);
        Assertions.assertThatThrownBy(() -> mock.boz().hoge())
                .isInstanceOf(SmartNullPointerException.class);
    }
}
