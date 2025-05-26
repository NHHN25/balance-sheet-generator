package com.gs.controllers.dao;

import com.gs.controllers.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao {
    Optional<Account> findById(int id) throws DaoException;
    Optional<Account> findByCode(String code) throws DaoException;
    List<Account> findAll() throws DaoException;
    void insert(Account account) throws DaoException;
    void update(Account account) throws DaoException;
    void delete(int id) throws DaoException;
}
