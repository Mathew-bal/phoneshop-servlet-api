package com.es.phoneshop.model.baseentity;

import java.io.Serializable;

public abstract class BaseEntity<T> implements Serializable {
    protected T id;

    public BaseEntity() {
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
