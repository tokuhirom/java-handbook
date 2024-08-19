package com.example.collection;


import com.google.common.collect.ImmutableList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListTest {
    @Test
    public void test() {
        // 空の immutable list を作成
        ImmutableList<Object> of = ImmutableList.of();
        // 幾つかの要素を含む Immutable list を作成
        ImmutableList<Integer> of1 = ImmutableList.of(1, 2, 3);
        // いっぱい入った immutable list をビルダー経由で作成
        ImmutableList<Object> build = ImmutableList.builder()
                .add(1)
                .add(2)
                .add(3)
                .addAll(of1)
                .build();
    }

    @Test
    public void test2() {
        // ImmutableList は null を許容しない
        Assertions.assertThatThrownBy(() -> {
            ImmutableList.of(1, 2, null);
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void java() {
        // 複数の要素からリストを作成。
        List<Integer> integers = Arrays.asList(1, 2, 3);

        //　これはつまりこれと同じことなので
        Integer[] integers1 = {
                1, 2, 3
        };
        List<Integer> integers2 = Arrays.asList(integers1);
        // Arrays.asList() は性能を良くするために、内部的には配列をそのまま使っています。Java の配列は　allocate 後にはリサイズ不可です。結果として、要素の入れ替えは可能ですが、`integers.add(4)` のように要素を追加することはできません。
    }

    @Test
    public void unmodifiableList() {
        Assertions.assertThatThrownBy(() -> {
            List<Integer> integers = Collections.unmodifiableList(Arrays.asList(1, 2, 3));
            integers.add(3);
        }).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void singletonList() {
        List<Integer> integers = Collections.singletonList(3);
    }
}
