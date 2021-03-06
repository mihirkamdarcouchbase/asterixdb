/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.asterix.lang.aql.rewrites;

import org.apache.asterix.common.exceptions.CompilationException;
import org.apache.asterix.lang.aql.visitor.AqlStatementRewriteVisitor;
import org.apache.asterix.lang.common.base.IStatementRewriter;
import org.apache.asterix.lang.common.base.Statement;
import org.apache.asterix.lang.common.struct.VarIdentifier;
import org.apache.asterix.metadata.declared.MetadataProvider;

class AqlStatementRewriter implements IStatementRewriter {

    private static final char VAR_PREFIX = '$';

    @Override
    public boolean isRewritable(Statement.Kind kind) {
        return kind == Statement.Kind.DELETE;
    }

    @Override
    public void rewrite(Statement stmt, MetadataProvider metadataProvider) throws CompilationException {
        if (stmt != null) {
            stmt.accept(AqlStatementRewriteVisitor.INSTANCE, metadataProvider);
        }
    }

    @Override
    public String toExternalVariableName(String statementParameterName) {
        return null;
    }

    @Override
    public String toFunctionParameterName(VarIdentifier paramVar) {
        String name = paramVar.getValue();
        if (name.charAt(0) != VAR_PREFIX) {
            throw new IllegalArgumentException(name);
        }
        return name.substring(1);
    }
}
