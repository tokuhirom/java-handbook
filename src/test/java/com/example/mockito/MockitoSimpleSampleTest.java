package com.example.mockito;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

interface Dog {
    String berk();
}

public class MockitoSimpleSampleTest {
    @Test
    public void mockByMockito() throws Exception {
        // Dog クラスをモッキングする
        Dog dog = mock(Dog.class);

        // berk メソッドを引数なしで呼んだ時には "わんわん” を返すように設定
        when(dog.berk()).thenReturn("わんわん");

        // 設定したものが返ってきていることを確認
        assertThat(dog.berk()).isEqualTo("わんわん");
    }

    @Test
    public void mockByJavaCode() throws Exception {
        // Dog クラスをモッキングする
        Dog dog = new Dog() {
            @Override
            public String berk() {
                return "わんわん";
            }
        };

        // 設定したものが返ってきていることを確認
        assertThat(dog.berk()).isEqualTo("わんわん");
    }

    @Test
    public void testThenThrowException() throws Exception {
        // Dog クラスをモッキングする
        Dog dog = mock(Dog.class);

        // 呼んだら IllegalStateException が発生するようにする
        when(dog.berk())
                .thenThrow(new IllegalStateException());

        // 設定したものが返ってきていることを確認
        assertThatThrownBy(dog::berk)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void testSpy() throws Exception {
        // Dog クラスをspyにする
        ArrayList<String> list = Mockito.spy(new ArrayList<>());

        // add メソッドを呼んでみる
        list.add("hoge");

        // そのメソッドが実際に呼ばれたかどうかを確認する
        verify(list).add("hoge");
    }
}
