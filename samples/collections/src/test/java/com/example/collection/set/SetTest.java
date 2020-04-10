package com.example.collection.set;

import com.google.common.collect.ImmutableSet;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetTest {

    @Test
    public void test() {
        ImmutableSet<Object> build = ImmutableSet.builder()
                .add("A")
                .add("X")
                .add("P")
                .build();
        System.out.println(build.toString());
    }

    @Test
    public void testOf() {
        ImmutableSet<Object> build = ImmutableSet.of("A", "X", "P");
        System.out.println(build.toString());
    }

    @Test
    public void immutableSetNull() {
        Assertions.assertThatThrownBy(() -> {
            ImmutableSet.of(null);
        }).isInstanceOf(NullPointerException.class);
    }


    @Test
    public void stream() {
        Set<String> build = Stream.of("A", "X", "P")
                .collect(Collectors.toSet());
        System.out.println(build.getClass().toString());
        System.out.println(build.toString());
    }

    @Test
    public void hashSet() {
        HashSet<String> objects = new HashSet<>();
        objects.add("A");
        objects.add("X");
        objects.add("P");
        System.out.println(objects);
    }

    @Test
    public void linkedHashSet() {
        LinkedHashSet<String> objects = new LinkedHashSet<>();
        objects.add("A");
        objects.add("X");
        objects.add("P");
        System.out.println(objects);
    }

    @Test
    public void treeSet() {
        TreeSet<String> objects = new TreeSet<>();
        objects.add("A");
        objects.add("X");
        objects.add("P");
        System.out.println(objects);
    }
}
