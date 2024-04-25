package com.kai.utils.ttlMap;

import com.google.common.base.Ticker;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TtlMap<K, V> extends TimerTask {

    private final ConcurrentHashMap<K, TtlValue<V>> map;
    private final Ticker ticker;
    private final TtlRemovalListener<K, V> removalListener;
    private long delay;
    private final Timer timer;

    protected TtlMap(TtlRemovalListener<K, V> listener, long delay, long period) {
        this.map = new ConcurrentHashMap<>();
        this.ticker = Ticker.systemTicker();
        this.timer = new Timer();
        timer.schedule(this, 0, period);
        this.removalListener = listener;
        this.delay = delay;
    }

    @Override
    public void run() {
        long read = ticker.read();
        Iterator<Map.Entry<K, TtlValue<V>>> iterable = map.entrySet().iterator();
        while (iterable.hasNext()) {
            Map.Entry<K, TtlValue<V>> next = iterable.next();
            TtlValue<V> value = next.getValue();
            K key = next.getKey();
            long t = value.t;
            if (read > t) {
                iterable.remove();
                if (value.vChange) {
                    removalListener.onRemoval(key, value.getV());
                }
            }
        }
    }

    public V put(K key, V value) {
        return put(key, value, false);
    }

    public V put(K key, V value, boolean vChange) {
        TtlValue<V> ttlValue;
        long now = ticker.read();
        if (map.containsKey(key)) {
            ttlValue = map.get(key);
            ttlValue.setV(value);
            ttlValue.setT(now + delay);
            ttlValue.setvChange(true);
        } else {
            ttlValue = new TtlValue<>(value, now + delay);
            ttlValue.setvChange(vChange);
            map.put(key, ttlValue);
        }
        return ttlValue.v;
    }

    public V get(K key) {
        TtlValue<V> ttlValue = map.get(key);
        if (ttlValue == null) {
            return null;
        }
        long now = ticker.read();
        ttlValue.setT(now + delay);
        return ttlValue.v;
    }

    public void remove(K key) {
        TtlValue<V> ttlValue = map.get(key);
        if (ttlValue == null) {
            return;
        }
        map.remove(key);
    }

    public boolean containsKey(K k) {
        return map.containsKey(k);
    }

    private static class TtlValue<V> {
        private long t;
        private V v;
        private boolean vChange = false;

        public TtlValue(V v, long t) {
            this.t = t;
            this.v = v;
        }

        public long getT() {
            return t;
        }

        public void setT(long t) {
            this.t = t;
        }

        public V getV() {
            return v;
        }

        public void setV(V v) {
            this.v = v;
        }

        public boolean isvChange() {
            return vChange;
        }

        public void setvChange(boolean vChange) {
            this.vChange = vChange;
        }
    }

    public List<Object> getValueList() {
        List<Object> list = new ArrayList<>();
        for (Map.Entry<K, TtlValue<V>> entry : map.entrySet()) {
            list.add(entry.getValue().v);
        }
        return list;
    }
}
