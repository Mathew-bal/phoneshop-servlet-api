package com.es.phoneshop.dao;

import com.es.phoneshop.model.baseentity.BaseEntity;

public interface GenericDao<T extends BaseEntity<Long>> {
    T get(Long id);
    void save(T item);
    void delete(Long id);
}
