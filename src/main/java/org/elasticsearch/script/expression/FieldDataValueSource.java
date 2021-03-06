/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.script.expression;


import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.elasticsearch.index.fielddata.AtomicFieldData;
import org.elasticsearch.index.fielddata.AtomicNumericFieldData;
import org.elasticsearch.index.fielddata.IndexFieldData;

import java.io.IOException;
import java.util.Map;

/**
 * A {@link ValueSource} wrapper for field data.
 */
class FieldDataValueSource extends ValueSource {

    IndexFieldData<?> fieldData;

    FieldDataValueSource(IndexFieldData<?> d) {
        fieldData = d;
    }

    @Override
    public FunctionValues getValues(Map context, LeafReaderContext leaf) throws IOException {
        AtomicFieldData leafData = fieldData.load(leaf);
        assert(leafData instanceof AtomicNumericFieldData);
        return new FieldDataFunctionValues(this, (AtomicNumericFieldData)leafData);
    }

    @Override
    public boolean equals(Object other) {
        return fieldData.equals(other);
    }

    @Override
    public int hashCode() {
        return fieldData.hashCode();
    }

    @Override
    public String description() {
        return "field(" + fieldData.getFieldNames().toString() + ")";
    }
}
