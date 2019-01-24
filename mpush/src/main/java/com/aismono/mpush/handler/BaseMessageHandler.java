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


import com.aismono.mpush.api.Message;
import com.aismono.mpush.api.MessageHandler;
import com.aismono.mpush.api.connection.Connection;
import com.aismono.mpush.api.protocol.Packet;

public abstract class BaseMessageHandler<T extends Message> implements MessageHandler {
    public abstract T decode(Packet packet, Connection connection);

    public abstract void handle(T message);

    @Override
    public void handle(Packet packet, Connection connection) {
        T t = decode(packet, connection);
        if (t != null) {
            t.decodeBody();
            handle(t);
        }
    }
}