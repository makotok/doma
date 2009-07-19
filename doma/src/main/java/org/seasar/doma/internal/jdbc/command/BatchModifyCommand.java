/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.seasar.doma.internal.jdbc.query.BatchModifyQuery;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.jdbc.BatchOptimisticLockException;
import org.seasar.doma.jdbc.BatchUniqueConstraintException;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public abstract class BatchModifyCommand<Q extends BatchModifyQuery> implements
        Command<int[], Q> {

    protected final Q query;

    public BatchModifyCommand(Q query) {
        assertNotNull(query);
        this.query = query;
    }

    @Override
    public int[] execute() {
        if (!query.isExecutable()) {
            return new int[] {};
        }
        Connection connection = Jdbcs.getConnection(query.getConfig()
                .dataSource());
        try {
            PreparedSql sql = query.getSql();
            PreparedStatement preparedStatement = Jdbcs
                    .prepareStatement(connection, sql.getRawSql(), query
                            .isAutoGeneratedKeysSupported());
            try {
                setupOptions(preparedStatement);
                return executeInternal(preparedStatement, query.getSqls());
            } catch (SQLException e) {
                throw new JdbcException(MessageCode.DOMA2009, e, sql
                        .getRawSql(), e);
            } finally {
                Jdbcs.close(preparedStatement, query.getConfig().jdbcLogger());
            }
        } finally {
            Jdbcs.close(connection, query.getConfig().jdbcLogger());
        }
    }

    protected abstract int[] executeInternal(
            PreparedStatement preparedStatement, List<PreparedSql> sqls)
            throws SQLException;

    protected void setupOptions(PreparedStatement preparedStatement)
            throws SQLException {
        if (query.getQueryTimeout() > 0) {
            preparedStatement.setQueryTimeout(query.getQueryTimeout());
        }
    }

    protected int[] executeBatch(PreparedStatement preparedStatement,
            List<PreparedSql> sqls) throws SQLException {
        int batchSize = query.getBatchSize();
        int sqlSize = sqls.size();
        int[] updatedRows = new int[sqlSize];
        int pos = 0;
        for (int i = 0; i < sqlSize; i++) {
            PreparedSql sql = sqls.get(i);
            log(sql);
            bindParameters(preparedStatement, sql);
            preparedStatement.addBatch();
            if (i == sqlSize - 1 || (batchSize > 0 && (i + 1) % batchSize == 0)) {
                int[] rows = executeBatch(preparedStatement, sql);
                validateRows(preparedStatement, sql, rows);
                System.arraycopy(rows, 0, updatedRows, pos, rows.length);
                pos = i + 1;
            }
        }
        return updatedRows;
    }

    protected int[] executeBatch(PreparedStatement preparedStatement,
            PreparedSql sql) throws SQLException {
        try {
            return preparedStatement.executeBatch();
        } catch (SQLException e) {
            Dialect dialect = query.getConfig().dialect();
            if (dialect.isUniqueConstraintViolated(e)) {
                throw new BatchUniqueConstraintException(sql, e);
            }
            throw e;
        }
    }

    protected void log(PreparedSql sql) {
        query.getConfig().jdbcLogger().logSql(query.getClassName(), query
                .getMethodName(), sql);
    }

    protected void bindParameters(PreparedStatement preparedStatement,
            PreparedSql sql) throws SQLException {
        PreparedSqlParameterBinder binder = new PreparedSqlParameterBinder(
                query);
        binder.bind(preparedStatement, sql.getParameters());
    }

    protected void validateRows(PreparedStatement preparedStatement,
            PreparedSql sql, int[] rows) throws SQLException {
        Dialect dialect = query.getConfig().dialect();
        if (dialect.supportsBatchUpdateResults()) {
            if (!query.isOptimisticLockCheckRequired()) {
                return;
            }
            for (int i = 0; i < rows.length; ++i) {
                if (rows[i] != 1) {
                    throw new BatchOptimisticLockException(sql);
                }
            }
        } else if (preparedStatement.getUpdateCount() == rows.length) {
            Arrays.fill(rows, 1);
        } else {
            if (!query.isOptimisticLockCheckRequired()) {
                return;
            }
            throw new BatchOptimisticLockException(sql);
        }
    }
}