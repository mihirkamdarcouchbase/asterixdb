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
package org.apache.asterix.app.message;

import org.apache.asterix.common.api.INcApplicationContext;
import org.apache.asterix.common.library.ILibraryManager;
import org.apache.asterix.common.messaging.CcIdentifiedMessage;
import org.apache.asterix.common.messaging.api.INCMessageBroker;
import org.apache.asterix.common.messaging.api.INcAddressedMessage;
import org.apache.asterix.common.metadata.DataverseName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractUdfMessage extends CcIdentifiedMessage implements INcAddressedMessage {

    protected final DataverseName dataverseName;
    protected final String libraryName;
    protected static final Logger LOGGER = LogManager.getLogger();

    private static final long serialVersionUID = 3L;

    private final long reqId;

    public AbstractUdfMessage(DataverseName dataverseName, String libraryName, long reqId) {
        this.dataverseName = dataverseName;
        this.libraryName = libraryName;
        this.reqId = reqId;
    }

    @Override
    public void handle(INcApplicationContext appCtx) {
        ILibraryManager mgr = appCtx.getLibraryManager();
        INCMessageBroker broker = (INCMessageBroker) appCtx.getServiceContext().getMessageBroker();
        try {
            handleAction(mgr, appCtx);
            broker.sendMessageToCC(getCcId(), new UdfResponseMessage(reqId, null));
        } catch (Exception e) {
            try {
                LOGGER.error("Error in UDF distribution", e);
                broker.sendMessageToPrimaryCC(new UdfResponseMessage(reqId, e));
            } catch (Exception f) {
                LOGGER.error("Unable to send failure response to CC", f);
            }
        }

    }

    protected abstract void handleAction(ILibraryManager mgr, INcApplicationContext appCtx) throws Exception;

}
