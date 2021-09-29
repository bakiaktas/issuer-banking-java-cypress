package com.baktas.issuerbanking.utility.base;

import org.springframework.stereotype.Service;

@Service
public interface BaseService<T, K> {
    T findById(K id);
    T saveOrUpdate(T T);
}