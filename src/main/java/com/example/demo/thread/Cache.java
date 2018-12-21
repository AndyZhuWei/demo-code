package com.example.demo.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: zhuwei
 * @Date:2018/12/20 11:00
 * @Description: 通过缓存示例说明读写锁的使用方式
 * 通过使用读写锁提升读操作的并发性，也保证每次写操作对所有的读写操作的可见性，同时简化了编程方式。
 */
public class Cache {

    static Map<String,Object> map = new HashMap<String,Object>();
    static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    static Lock r = rwl.readLock();
    static Lock w = rwl.writeLock();
    //获取一个key对应的value
    public static final Object get(String key) {
        r.lock();
        try {
            return map.get(key);
        } finally {
            r.unlock();
        }
    }

    //设置key对应的value,并返回旧的value
    public static final Object put(String key,Object value) {
        w.lock();
        try {
            return map.put(key,value);
        } finally {
            w.unlock();
        }
    }

    //清空所有的内容
    public static final void clear() {
        w.lock();
        try {
            map.clear();
        } finally {
            w.unlock();
        }
    }



}
