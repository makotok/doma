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
package org.seasar.doma.jdbc.dialect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.jdbc.dialect.PostgresForUpdateTransformer;
import org.seasar.doma.internal.jdbc.dialect.PostgresPagingTransformer;
import org.seasar.doma.internal.jdbc.sql.PreparedSql;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlParameter;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.type.AbstractResultSetType;
import org.seasar.doma.jdbc.type.JdbcType;

/**
 * @author taedium
 * 
 */
public class PostgresDialect extends StandardDialect {

    protected static final String UNIQUE_CONSTRAINT_VIOLATION_STATE_CODE = "23505";

    protected static final JdbcType<ResultSet> RESULT_SET = new PostgresResultSetType();

    public PostgresDialect() {
        this(new PostgresJdbcMappingVisitor(),
                new PostgresSqlLogFormattingVisitor());
    }

    public PostgresDialect(JdbcMappingVisitor jdbcMappingVisitor,
            SqlLogFormattingVisitor sqlLogFormattingVisitor) {
        super(jdbcMappingVisitor, sqlLogFormattingVisitor);
    }

    @Override
    public String getName() {
        return "postgres";
    }

    @Override
    protected SqlNode toForUpdateSqlNode(SqlNode sqlNode,
            SelectForUpdateType forUpdateType, int waitSeconds,
            String... aliases) {
        PostgresForUpdateTransformer transformer = new PostgresForUpdateTransformer(
                forUpdateType, waitSeconds, aliases);
        return transformer.transform(sqlNode);
    }

    @Override
    protected SqlNode toPagingSqlNode(SqlNode sqlNode, int offset, int limit) {
        PostgresPagingTransformer transformer = new PostgresPagingTransformer(
                offset, limit);
        return transformer.transform(sqlNode);
    }

    @Override
    public boolean isUniqueConstraintViolated(SQLException sqlException) {
        if (sqlException == null) {
            throw new DomaIllegalArgumentException("sqlException", sqlException);
        }
        String state = getSQLState(sqlException);
        return UNIQUE_CONSTRAINT_VIOLATION_STATE_CODE.equals(state);
    }

    @Override
    public PreparedSql getIdentitySelectSql(String qualifiedTableName,
            String columnName) {
        if (qualifiedTableName == null) {
            throw new DomaIllegalArgumentException("qualifiedTableName",
                    qualifiedTableName);
        }
        if (columnName == null) {
            throw new DomaIllegalArgumentException("columnName", columnName);
        }
        StringBuilder buf = new StringBuilder(64);
        buf.append("select currval('");
        buf.append(qualifiedTableName);
        buf.append('_').append(columnName);
        buf.append("_seq')");
        String rawSql = buf.toString();
        return new PreparedSql(rawSql, rawSql, Collections
                .<PreparedSqlParameter> emptyList());
    }

    @Override
    public PreparedSql getSequenceNextValSql(String qualifiedSequenceName,
            long allocationSize) {
        if (qualifiedSequenceName == null) {
            throw new DomaIllegalArgumentException("qualifiedSequenceName",
                    qualifiedSequenceName);
        }
        String rawSql = "select nextval('" + qualifiedSequenceName + "')";
        return new PreparedSql(rawSql, rawSql, Collections
                .<PreparedSqlParameter> emptyList());
    }

    @Override
    public boolean supportsIdentity() {
        return true;
    }

    @Override
    public boolean supportsSequence() {
        return true;
    }

    @Override
    public boolean supportsSelectForUpdate(SelectForUpdateType type,
            boolean withTargets) {
        return type == SelectForUpdateType.NORMAL;
    }

    @Override
    public boolean supportsResultSetReturningAsOutParameter() {
        return true;
    }

    @Override
    public JdbcType<ResultSet> getResultSetType() {
        return RESULT_SET;
    }

    public static class PostgresResultSetType extends AbstractResultSetType {

        public PostgresResultSetType() {
            super(Types.OTHER);
        }
    }

    public static class PostgresJdbcMappingVisitor extends
            StandardJdbcMappingVisitor {
    }

    public static class PostgresSqlLogFormattingVisitor extends
            StandardSqlLogFormattingVisitor {
    }

}