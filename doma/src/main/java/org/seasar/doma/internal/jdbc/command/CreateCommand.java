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

import java.sql.Connection;
import java.sql.SQLException;

import org.seasar.doma.internal.jdbc.query.CreateQuery;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.MessageCode;

/**
 * @author taedium
 * 
 */
public class CreateCommand<R> implements Command<R, CreateQuery<R>> {

    protected final CreateQuery<R> query;

    public CreateCommand(CreateQuery<R> query) {
        this.query = query;
    }

    @Override
    public R execute() {
        Connection connection = Jdbcs.getConnection(query.getConfig()
                .dataSource());
        try {
            return query.create(connection);
        } catch (SQLException e) {
            throw new JdbcException(MessageCode.DOMA2008, e, e);
        } finally {
            Jdbcs.close(connection, query.getConfig().jdbcLogger());
        }
    }

}