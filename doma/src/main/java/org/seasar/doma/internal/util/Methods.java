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
package org.seasar.doma.internal.util;

import static org.seasar.doma.internal.util.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.seasar.doma.internal.WrapException;


/**
 * @author taedium
 * 
 */
public final class Methods {

    public static <T> T invoke(Method method, Object target, Object... params)
            throws WrapException {
        assertNotNull(method);
        try {
            @SuppressWarnings("unchecked")
            T result = (T) method.invoke(target, params);
            return result;
        } catch (IllegalArgumentException e) {
            throw new WrapException(e);
        } catch (IllegalAccessException e) {
            throw new WrapException(e);
        } catch (InvocationTargetException e) {
            throw new WrapException(e);
        }
    }

    public static Method getMethod(Class<?> clazz, String name,
            Class<?>... parameterTypes) throws WrapException {
        assertNotNull(clazz, name, parameterTypes);
        try {
            return clazz.getMethod(name, parameterTypes);
        } catch (SecurityException e) {
            throw new WrapException(e);
        } catch (NoSuchMethodException e) {
            throw new WrapException(e);
        }
    }
}