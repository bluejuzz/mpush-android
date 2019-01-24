/*
 * (C) Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     ohun@live.cn (夜色)
 */

package com.aismono.mpush.handler;


import com.aismono.mpush.api.ClientListener;
import com.aismono.mpush.api.Logger;
import com.aismono.mpush.api.connection.Connection;
import com.aismono.mpush.api.protocol.Packet;
import com.aismono.mpush.client.ClientConfig;
import com.aismono.mpush.message.AckMessage;
import com.aismono.mpush.message.PushMessage;

/**
 * Created by ohun on 2015/12/30.
 *
 * @author ohun@live.cn (夜色)
 */
public final class PushMessageHandler extends BaseMessageHandler<PushMessage> {
    private final Logger logger = ClientConfig.I.getLogger();
    private final ClientListener listener = ClientConfig.I.getClientListener();

    @Override
    public PushMessage decode(Packet packet, Connection connection) {
        return new PushMessage(packet, connection);
    }

    @Override
    public void handle(PushMessage message) {
        logger.d(">>> receive push message=%s", message.content.length);
        listener.onReceivePush(message.getConnection().getClient(),
                message.content,
                message.bizAck() ? message.getSessionId() : 0);
        if (message.autoAck()) {
            AckMessage.from(message).sendRaw();
            logger.d("<<< send ack for push messageId=%d", message.getSessionId());
        }
    }
}
