package com.gs.controllers.dao;

import com.gs.controllers.model.TransactionEntry;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Data-access operations for TransactionEntry records.
 */
public interface TransactionDao {

    /**
     * Finds a transaction by its primary key.
     *
     * @param id the transaction ID
     * @return an Optional containing the entry if found, or empty if not
     * @throws DaoException on any persistence error
     */
    Optional<TransactionEntry> findById(int id) throws DaoException;

    /**
     * Retrieves all transactions between two dates, inclusive.
     *
     * @param from start date (inclusive)
     * @param to   end date (inclusive)
     * @return list of matching TransactionEntry objects
     * @throws DaoException on any persistence error
     */
    List<TransactionEntry> findByDateRange(LocalDate from, LocalDate to) throws DaoException;

    /**
     * Retrieves all transactions in the system.
     *
     * @return list of all TransactionEntry objects
     * @throws DaoException on any persistence error
     */
    List<TransactionEntry> findAll() throws DaoException;

    /**
     * Inserts a new transaction entry into the ledger.
     * On success, the generated ID should be set on the passed-in object.
     *
     * @param tx the transaction to insert
     * @throws DaoException on any persistence error
     */
    void insert(TransactionEntry tx) throws DaoException;

    /**
     * Updates an existing transaction record.
     *
     * @param tx the transaction with updated fields (must have a valid ID)
     * @throws DaoException on any persistence error
     */
    void update(TransactionEntry tx) throws DaoException;

    /**
     * Deletes the transaction with the given ID.
     *
     * @param id the ID of the transaction to remove
     * @throws DaoException on any persistence error
     */
    void delete(int id) throws DaoException;

}
