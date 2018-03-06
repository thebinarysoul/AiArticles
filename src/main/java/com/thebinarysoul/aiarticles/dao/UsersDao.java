package com.thebinarysoul.aiarticles.dao;

import java.util.List;

public interface UsersDao extends AbstractDao<Long, Long> {
    List<Long> readAll();
}
