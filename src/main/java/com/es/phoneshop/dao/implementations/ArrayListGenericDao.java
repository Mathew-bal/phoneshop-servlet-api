package com.es.phoneshop.dao.implementations;

import com.es.phoneshop.dao.GenericDao;
import com.es.phoneshop.locker.ExtendedReadWriteLock;
import com.es.phoneshop.model.baseentity.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class ArrayListGenericDao<T extends BaseEntity<Long>> implements GenericDao<T> {

    protected List<T> items;

    protected long maxId = 1;

    protected ExtendedReadWriteLock extendedReadWriteLock;

    Supplier<? extends RuntimeException> exceptionSupplier;

    public ArrayListGenericDao(Supplier<? extends RuntimeException> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
        items = new ArrayList<T>();
        extendedReadWriteLock = new ExtendedReadWriteLock();
    }

    @Override
    public T get(Long id) {
        return extendedReadWriteLock.readSafe(() -> items.stream().
                filter(item -> id.equals(item.getId())).
                findAny().orElseThrow(exceptionSupplier));
    }

    @Override
    public void save(T item) {
        extendedReadWriteLock.writeSafe((saveItem) -> {
            if (saveItem.getId() == null) {
                saveItem.setId(maxId++);
                items.add(saveItem);
            } else {
                items = items.stream().map((oldProduct) ->
                {
                    if (saveItem.getId().equals(oldProduct.getId()))
                        return saveItem;
                    else
                        return oldProduct;
                }).collect(Collectors.toList());
            }
        }, item);
    }

    @Override
    public void delete(Long id) {
        extendedReadWriteLock.writeSafe((deleteId) -> {
            items.removeIf(item -> deleteId.equals(item.getId()));
        }, id);
    }
}
