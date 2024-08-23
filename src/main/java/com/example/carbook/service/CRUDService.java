package com.example.carbook.service;

import java.util.List;

public interface CRUDService<T, ID> {

    T findById(ID id);

    List<T> findAll();

    T save(T t);

    T update(ID id, T dto);

    void deleteById(ID id);

}
