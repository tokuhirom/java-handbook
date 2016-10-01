package com.example;

import lombok.NonNull;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflectionDemo {
    @Test
    public void getClassFromInstance() {
        ReflectionDemo reflectionDemo = new ReflectionDemo();
        Class<? extends ReflectionDemo> aClass = reflectionDemo.getClass();
    }

    @Test
    public void getClassFromClass() {
        Class<ReflectionDemo> aClass = ReflectionDemo.class;
    }

    @Test
    public void printMethods() {
        Class<MyClass> aClass = MyClass.class;
        System.out.println("--- getMethods() ---");
        for (Method method : aClass.getMethods()) {
            System.out.println(stringifyMethod(method));
        }
        System.out.println("--- getDeclaredMethods() ---");
        for (Method method : aClass.getDeclaredMethods()) {
            System.out.println(stringifyMethod(method));
        }
    }

    private String stringifyMethod(Method method) {
        return method.getDeclaringClass() + "." + method.getName() + "("
                + Stream.of(method.getParameters())
                .map(it -> it.getType().getSimpleName() + " " + it.getName())
                .collect(Collectors.joining(", "))
                + ")";
    }

    @Test
    public void getMethodDetails() {
        Class<MyClass> aClass = MyClass.class;
        System.out.println("--- getDeclaredMethods() ---");
        for (Method method : aClass.getDeclaredMethods()) {
            System.out.println(method.getName());
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                System.out.println("ANNOTATION: " + annotation);
            }

            for (Parameter parameter : method.getParameters()) {
                System.out.println(parameter.getName());
                System.out.println(parameter.isNamePresent());
                System.out.println(parameter.getType());
            }
        }
    }

    @Test
    public void getFields() {
        Class<MyClass> aClass = MyClass.class;
        // 定義されている public フィールドの配列を得ます
        System.out.println("--- getFields() ---");
        for (Field field : aClass.getFields()) {
            System.out.println(field.getName());
        }
        // このクラスに定義されているフィールドの配列を得ます。
        // public, protected, package private, private のものも得られます
        System.out.println("--- getDeclaredFields() ---");
        for (Field field : aClass.getDeclaredFields()) {
            System.out.println(field.getName());
        }
    }

    public static class MyClass {
        private String privateField;
        public String publicField;

        @Ignore
        public String publicMethod(@NonNull String name) {
            return "Hello, " + name.toUpperCase();
        }

        private String secretMethod(@NonNull String name) {
            return "Hello, " + name.toUpperCase();
        }
    }
}
