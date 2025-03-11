package com.toy.backend.service;

import java.util.List;
import java.util.UUID;

public interface BaseService<E> {
    public List<E> list();
    public E select(Long no);
    public E selectById(String id);
    public boolean insert(E entity);
    public boolean update(E entity);
    public boolean updateById(E entity);
    public boolean delete(Long no);
    public boolean deleteById(String id);
}
