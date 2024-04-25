package com.kai.utils.TtlMap;

import com.kai.utils.ttlMap.TtlMap;
import com.kai.utils.ttlMap.TtlMapBuilder;
import com.kai.utils.ttlMap.TtlRemovalListener;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class Test2 {
    static TtlMap<String, String> ttlMap;

    @Test
    public void test() {
        Thread thread1 = new Thread(() -> {
            test(1);
        });
        Thread thread2 = new Thread(() -> {
            test(2);
        });
        Thread thread3 = new Thread(() -> {
            test(3);
        });
        thread1.start();
        thread2.start();
        thread3.start();
    }

    public static void test(int i) {
        if (ttlMap == null) {
            TtlRemovalListener<String, String> listener = (key, value) -> {
                System.out.println("key:" + key + " value:" + value);
            };
            ttlMap = new TtlMapBuilder<String, String>().setListener(listener).setDelay(3, TimeUnit.SECONDS).setPeriod(1, TimeUnit.SECONDS).build();
        }
        ttlMap.put(String.valueOf(i), Thread.currentThread().getName() + ":" + System.currentTimeMillis(), true);
    }
}
