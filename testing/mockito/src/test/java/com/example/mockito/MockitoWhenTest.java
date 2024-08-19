package com.example.mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

interface Printer {
    boolean printMessage(String message);

    String boo(String message);
}

@ExtendWith(MockitoExtension.class)
public class MockitoWhenTest {
    // 引数に実オブジェクトを渡す場合
    @Test
    public void test() {
        Printer printer = mock(Printer.class);
        when(printer.printMessage("hoge"))
                .thenReturn(true);
        assertThat(printer.printMessage("hoge"))
                .isTrue();
    }

    // 引数にanyStringとか使う場合
    @Test
    public void testAnyString() {
        Printer printer = mock(Printer.class);
        when(printer.printMessage(anyString()))
                .thenReturn(true);
        assertThat(printer.printMessage("hoge"))
                .isTrue();
    }
}
