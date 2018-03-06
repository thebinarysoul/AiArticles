package com.thebinarysoul.aiarticles.dao;

import java.util.Optional;

public interface AbstractDao<C, O> {
    void save(C t);
    Optional<O> read(O o);
}
