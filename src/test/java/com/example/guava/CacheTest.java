package com.example.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by tokuhirom on 9/7/16.
 */
public class CacheTest {
    private static final RemovalListener<? super Object, ? super Object> MY_LISTENER = notification -> {
        System.out.println("Removed cache item");
    };

    @Test
    public void test() throws ExecutionException, InterruptedException {
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .removalListener(MY_LISTENER)
                .build(
                        new CacheLoader<String, String>() {
                            @Override
                            public String load(String key) throws Exception {
                                System.out.println("Generating new item: " + key);
                                return key + "!!!";
                            }
                        });

        System.out.println(cache.get("hoge"));
        System.out.println(cache.get("hoge"));
        System.out.println(cache.get("fuga"));
    }
}
