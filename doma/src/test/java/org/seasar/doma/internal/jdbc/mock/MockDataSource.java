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
package org.seasar.doma.internal.jdbc.mock;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.doma.internal.util.Assertions;


/**
 * 
 * @author taedium
 * 
 */
public class MockDataSource extends MockWrapper implements DataSource {

    public MockConnection connection = new MockConnection();

    public MockDataSource() {
    }

    public MockDataSource(MockConnection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password)
            throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        Assertions.notYetImplemented();
        return 0;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        Assertions.notYetImplemented();
        return null;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        Assertions.notYetImplemented();

    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        Assertions.notYetImplemented();

    }

}