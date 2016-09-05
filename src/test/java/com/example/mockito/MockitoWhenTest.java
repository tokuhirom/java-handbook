package com.example.mockito;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

interface Printer {
    boolean printMessage(String message);

    String boo(String message);
}

@RunWith(MockitoJUnitRunner.class)
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

    // void な場合
    @Test
    public void foo() {
        MockitoAnnotations.initMocks(this);
        Printer mock = mock(Printer.class);
        String s = mock.boo("hoge");
        s.startsWith("bar");
        assertThat(s).isEqualTo("hoge");
    }
}
