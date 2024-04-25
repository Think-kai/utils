package com.kai.utils.ttlMap;

import java.util.concurrent.TimeUnit;

public class TtlMapBuilder<K, V> {
    private TtlRemovalListener<K, V> listener;
    private long delay;
    private long period;

    public TtlMapBuilder() {
    }

    public TtlMapBuilder<K, V> setListener(TtlRemovalListener<K, V> listener) {
        this.listener = listener;
        return this;
    }

    public TtlMapBuilder<K, V> setDelay(long delay, TimeUnit unit) {
        this.delay = unit.toNanos(delay);
        return this;
    }

    public TtlMapBuilder<K, V> setPeriod(long period, TimeUnit unit) {
        this.period = unit.toMillis(period);
        return this;
    }

    public TtlMap<K, V> build(){
        return new TtlMap<>(this.listener, this.period, this.delay);
    }
}
