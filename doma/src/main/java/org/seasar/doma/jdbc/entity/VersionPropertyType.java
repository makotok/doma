/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.doma.jdbc.entity;

import org.seasar.doma.wrapper.NumberWrapper;

/**
 * バージョンのプロパティ型です。
 * 
 * @author taedium
 * 
 */
public abstract class VersionPropertyType<E, V extends Number> extends
        BasicPropertyType<E, V> {

    /**
     * インスタンスを構築します。
     * 
     * @param name
     *            プロパティの名前
     * @param columnName
     *            カラム名
     */
    public VersionPropertyType(String name, String columnName) {
        super(name, columnName, true, true);
    }

    @Override
    public boolean isVersion() {
        return true;
    }

    /**
     * 必要であればバージョンの値を設定します。
     * 
     * @param entity
     *            エンティティ
     * @param value
     *            バージョンの値
     */
    public void setIfNecessary(E entity, Number value) {
        NumberWrapper<?> wrapper = getWrapper(entity);
        if (wrapper.get() == null || wrapper.get().intValue() < 0) {
            wrapper.set(value);
        }
    }

    /**
     * バージョン番号をインクリメントします。
     * 
     * @param entity
     *            エンティティ
     */
    public void increment(E entity) {
        NumberWrapper<?> wrapper = getWrapper(entity);
        if (wrapper.get() != null) {
            int i = wrapper.get().intValue();
            wrapper.set(i + 1);
        }
    }

    @Override
    public abstract NumberWrapper<V> getWrapper(E entity);
}