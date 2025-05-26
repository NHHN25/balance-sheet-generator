package com.gs.controllers.dao.jdbc;

import com.gs.controllers.DbUtil;
import com.gs.controllers.dao.AccountDao;
import com.gs.controllers.dao.DaoException;
import com.gs.controllers.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcAccountDao implements AccountDao {
    private static final String SELECT_BY_ID    =
            "SELECT id, code, name, type FROM account WHERE id = ?";

    private static final String SELECT_BY_CODE  =
            "SELECT id, code, name, type FROM account WHERE code = ?";

    private static final String SELECT_ALL      =
            "SELECT id, code, name, type FROM account ORDER BY code";

    private static final String INSERT_SQL      =
            "INSERT INTO account (code, name, type) VALUES (?, ?, ?)";

    private static final String UPDATE_SQL      =
            "UPDATE account SET code = ?, name = ?, type = ? WHERE id = ?";

    private static final String DELETE_SQL      =
            "DELETE FROM account WHERE id = ?";

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Account> findById(int id) throws DaoException {
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
            throw new DaoException("Error querying account by id: " + id, e);
        }
    }

    /**
     * @param code
     * @return
     */
    @Override
    public Optional<Account> findByCode(String code) throws DaoException {
        try (Connection conn = DbUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(SELECT_BY_CODE)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new DaoException("Error querying account by code: " + code, e);
        }
    }

    /**
     * @return
     */
    @Override
    public List<Account> findAll() throws DaoException {
        List<Account> accounts = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
        ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                accounts.add(mapRow(rs));
            }
            return accounts;
        } catch (Exception e) {
            throw new DaoException("Error querying all accounts", e);
        }
    }

    /**
     * @param account
     */
    @Override
    public void insert(Account account) throws DaoException {
        try (Connection conn = DbUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, account.getCode());
            ps.setString(2, account.getName());
            ps.setString(3, account.getType());
            int affectedRows = ps.executeUpdate();
            if (affectedRows != 1) throw new DaoException("Insert affected " + affectedRows + " rows, expected 1");

            try(ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    account.setId(rs.getInt(1));
                }
            }
        } catch (Exception e) {
            throw new DaoException("Error inserting account", e);
        }

    }

    /**
     * @param account
     */
    @Override
    public void update(Account account) throws DaoException {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, account.getCode());
            ps.setString(2, account.getName());
            ps.setString(3, account.getType());
            ps.setInt(4, account.getId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows != 1) throw new DaoException("Insert affected " + affectedRows + " rows, expected 1");

        } catch (Exception e) {
            throw new DaoException("Error updating account",e);
        }

    }

    /**
     * @param id
     */
    @Override
    public void delete(int id) throws DaoException {
        try (Connection conn = DbUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows != 1) throw new DaoException("Insert affected " + affectedRows + " rows, expected 1");

        } catch (Exception e) {
            throw new DaoException("Error deleting account", e);
        }

    }

    /**
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private Account mapRow(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("id"),
                rs.getString("code"),
                rs.getString("name"),
                rs.getString("type")
        );
    }

}
