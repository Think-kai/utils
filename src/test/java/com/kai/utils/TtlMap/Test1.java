package com.kai.utils.TtlMap;

import com.kai.utils.ttlMap.TtlMap;
import com.kai.utils.ttlMap.TtlMapBuilder;
import com.kai.utils.ttlMap.TtlRemovalListener;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class Test1 {

    static MyMap myMap;

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            if (myMap == null) {
                System.out.println("not");
            } else {
                System.out.println("exists");
            }
        });
        Thread thread2 = new Thread(() -> {
            if (myMap == null) {
                System.out.println("not");
            } else {
                System.out.println("exists");
            }
        });
        Thread thread3 = new Thread(() -> {
            if (myMap == null) {
                System.out.println("not");
            } else {
                System.out.println("exists");
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();
    }

    private class MyMap {
        String key;
        String value;
    }
}
