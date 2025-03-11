package com.es.phoneshop.model.product;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ExtendedReadWriteLock {

    private final ReadWriteLock readWriteLock;

    private final Lock readLock;

    private final Lock writeLock;

    public ExtendedReadWriteLock(){
        readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    public <T> T readSafe(Supplier<T> readFunction){
        readLock.lock();
        try {
            return readFunction.get();
        }finally {
            readLock.unlock();
        }
    }

    public <T> void writeSafe(Consumer<T> writeFunction, T writeObject){
        writeLock.lock();
        try {
            writeFunction.accept(writeObject);
        }finally {
            writeLock.unlock();
        }
    }
}
