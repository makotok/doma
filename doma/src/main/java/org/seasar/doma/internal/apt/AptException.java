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
package org.seasar.doma.internal.apt;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

import org.seasar.doma.DomaException;
import org.seasar.doma.message.MessageCode;


/**
 * @author taedium
 * 
 */
public class AptException extends DomaException {

    private static final long serialVersionUID = 1L;

    protected final Kind kind;

    protected final Element element;

    public AptException(MessageCode messageCode, ProcessingEnvironment env,
            Element element, Object... args) {
        this(messageCode, env, element, null, args);
    }

    public AptException(MessageCode messageCode, ProcessingEnvironment env,
            Kind kind, Element element, Object... args) {
        this(messageCode, env, kind, element, null, args);
    }

    public AptException(MessageCode messageCode, ProcessingEnvironment env,
            Element element, Throwable cause, Object... args) {
        this(messageCode, env, Kind.ERROR, element, cause, args);
    }

    public AptException(MessageCode messageCode, ProcessingEnvironment env,
            Kind kind, Element element, Throwable cause, Object... args) {
        super(messageCode, cause, args);
        this.kind = kind;
        this.element = element;
        if (Options.isDebugEnabled(env)) {
            Notifier.debug(env, MessageCode.DOMA4074, messageCode, cause);
        }
    }

    public Kind getKind() {
        return kind;
    }

    public Element getElement() {
        return element;
    }

}