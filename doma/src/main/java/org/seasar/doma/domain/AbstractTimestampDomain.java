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
package org.seasar.doma.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;

import org.seasar.doma.DomaIllegalArgumentException;

/**
 * @author taedium
 * 
 */
public abstract class AbstractTimestampDomain<D extends AbstractTimestampDomain<D>>
        extends AbstractComparableDomain<Timestamp, D> implements
        SerializableDomain<Timestamp, D> {

    private static final long serialVersionUID = 1L;

    protected AbstractTimestampDomain() {
        this(null);
    }

    protected AbstractTimestampDomain(Timestamp value) {
        super(Timestamp.class, value);
    }

    @Override
    public Timestamp get() {
        if (value == null) {
            return null;
        }
        Timestamp timestamp = new Timestamp(value.getTime());
        timestamp.setNanos(value.getNanos());
        return timestamp;
    }

    @Override
    protected void setInternal(Timestamp v) {
        if (v == null) {
            super.setInternal(v);
        } else {
            Timestamp timestamp = new Timestamp(v.getTime());
            timestamp.setNanos(v.getNanos());
            super.setInternal(timestamp);
        }
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (AbstractTimestampDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AbstractTimestampDomainVisitor<R, P, TH> v = AbstractTimestampDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractTimestampDomain(this, p);
        }
        return visitor.visitUnknownDomain(this, p);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        AbstractTimestampDomain<?> other = AbstractTimestampDomain.class
                .cast(o);
        if (value == null) {
            return other.value == null;
        }
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : null;
    }

    private void readObject(ObjectInputStream inputStream) throws IOException,
            ClassNotFoundException {
        inputStream.defaultReadObject();
        value = Timestamp.class.cast(inputStream.readObject());
        changed = inputStream.readBoolean();
    }

    private void writeObject(ObjectOutputStream outputStream)
            throws IOException {
        outputStream.defaultWriteObject();
        outputStream.writeObject(value);
        outputStream.writeBoolean(changed);
    }

}