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
package org.seasar.doma.jdbc;


/**
 * @author taedium
 * 
 */
public abstract class DomaAbstractConfig implements Config {

    protected static NameConvention nameConvention = new BuiltinNameConvention();

    protected static SqlFileRepository sqlFileRepository = new BuiltinSqlFileRepository();

    protected static JdbcLogger jdbcLogger = new BuiltinJdbcLogger();

    protected static RequiresNewController requiresNewController = new BuiltinRequiresNewController();

    @Override
    public String dataSourceName() {
        return getClass().getName();
    }

    @Override
    public NameConvention nameConvention() {
        return nameConvention;
    }

    @Override
    public SqlFileRepository sqlFileRepository() {
        return sqlFileRepository;
    }

    @Override
    public JdbcLogger jdbcLogger() {
        return jdbcLogger;
    }

    @Override
    public RequiresNewController requiresNewController() {
        return requiresNewController;
    }

    @Override
    public int fetchSize() {
        return 0;
    }

    @Override
    public int maxRows() {
        return 0;
    }

    @Override
    public int queryTimeout() {
        return 0;
    }

    public int batchSize() {
        return 10;
    }
}