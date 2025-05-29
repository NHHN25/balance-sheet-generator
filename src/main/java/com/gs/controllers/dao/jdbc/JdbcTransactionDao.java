package com.gs.controllers.dao.jdbc;

import com.gs.controllers.DbUtil;
import com.gs.controllers.dao.DaoException;
import com.gs.controllers.dao.TransactionDao;
import com.gs.controllers.model.TransactionEntry;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTransactionDao implements TransactionDao {
    private static final String SELECT_BY_ID =
            "SELECT id, account_id, amount, entry_date, description FROM transaction_entry WHERE id = ?";

    private static final String SELECT_BY_DATE_RANGE =
            "SELECT id, account_id, amount, entry_date, description FROM transaction_entry WHERE entry_date BETWEEN ? AND ? ORDER BY entry_date, id";

    private static final String SELECT_ALL =
            "SELECT id, account_id, amount, entry_date, description FROM transaction_entry ORDER BY entry_date, id";

    private static final String INSERT_SQL =
            "INSERT INTO transaction_entry (account_id, amount, entry_date, description) VALUES (?,?,?,?)";

    private static final String UPDATE_SQL =
            "UPDATE transaction_entry SET account_id = ?, amount = ?, entry_date = ?, description = ? WHERE id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM transaction_entry WHERE id = ?";

    /**
     * Finds a transaction by its primary key.
     *
     * @param id the transaction ID
     * @return an Optional containing the entry if found, or empty if not
     * @throws DaoException on any persistence error
     */
    @Override
    public Optional<TransactionEntry> findById(int id) throws DaoException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new DaoException("Error querying transaction by id: " + id, e);
        }
    }

    /**
     * Retrieves all transactions between two dates, inclusive.
     *
     * @param from start date (inclusive)
     * @param to   end date (inclusive)
     * @return list of matching TransactionEntry objects
     * @throws DaoException on any persistence error
     */
    @Override
    public List<TransactionEntry> findByDateRange(LocalDate from, LocalDate to) throws DaoException {
        List<TransactionEntry> query = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(SELECT_BY_DATE_RANGE)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    query.add(mapRow(rs));
                }
                return query;
            }
        } catch (Exception e) {
            throw new DaoException(String.format("Error querying transactions between %s and %s", from, to), e);
        }
    }

    /**
     * Retrieves all transactions in the system.
     *
     * @return list of all TransactionEntry objects
     * @throws DaoException on any persistence error
     */
    @Override
    public List<TransactionEntry> findAll() throws DaoException {
        List<TransactionEntry> query = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                query.add(mapRow(rs));
            }
            return query;
        } catch (Exception e) {
            throw new DaoException("Error querying all transactions", e);
        }
    }

    /**
     * Inserts a new transaction entry into the ledger.
     * On success, the generated ID should be set on the passed-in object.
     *
     * @param tx the transaction to insert
     * @throws DaoException on any persistence error
     */
    @Override
    public void insert(TransactionEntry tx) throws DaoException {
        try (Connection conn = DbUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, tx.getAccountId());
            ps.setBigDecimal(2, tx.getAmount());
            ps.setDate(3, Date.valueOf(tx.getEntryDate()));
            ps.setString(4, tx.getDescription());
            int affectedRows = ps.executeUpdate();
            if (affectedRows != 1) {
                throw new DaoException("Insert affected " + affectedRows + " rows; expected 1");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    tx.setId(rs.getInt(1));
                }
            }
        } catch (Exception e) {
            throw new DaoException("Error inserting transaction: " + tx, e);
        }

    }

    /**
     * Updates an existing transaction record.
     *
     * @param tx the transaction with updated fields (must have a valid ID)
     * @throws DaoException on any persistence error
     */
    @Override
    public void update(TransactionEntry tx) throws DaoException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setInt(1, tx.getAccountId());
            ps.setBigDecimal(2, tx.getAmount());
            ps.setDate(3, Date.valueOf(tx.getEntryDate()));
            ps.setString(4, tx.getDescription());
            ps.setInt(5, tx.getId());
            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DaoException("Update affected " + affected + " rows; expected 1");
            }
        } catch (Exception e) {
            throw new DaoException("Error updating transaction: " + tx, e);
        }
    }

    /**
     * Deletes the transaction with the given ID.
     *
     * @param id the ID of the transaction to remove
     * @throws DaoException on any persistence error
     */
    @Override
    public void delete(int id) throws DaoException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected != 1) {
                throw new DaoException("Delete affected " + affected + " rows; expected 1");
            }
        } catch (Exception e) {
            throw new DaoException("Error deleting with transaction id =" + id, e);
        }
    }

    /** Helper to map a ResultSet row into a TransactionEntry object */
    private TransactionEntry mapRow(ResultSet rs) throws SQLException {
        TransactionEntry tx = new TransactionEntry();
        tx.setId(rs.getInt("id"));
        tx.setAccountId(rs.getInt("account_id"));
        tx.setAmount(rs.getBigDecimal("amount"));
        tx.setEntryDate(rs.getDate("entry_date").toLocalDate());
        tx.setDescription(rs.getString("description"));
        return tx;
    }


}
