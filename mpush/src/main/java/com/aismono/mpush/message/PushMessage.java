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

package com.aismono.mpush.message;


import com.aismono.mpush.api.connection.Connection;
import com.aismono.mpush.api.protocol.Command;
import com.aismono.mpush.api.protocol.Packet;

/**
 * Created by ohun on 2015/12/30.
 *
 * @author ohun@live.cn (夜色)
 */
public final class PushMessage extends BaseMessage {

    public byte[] content;

    public PushMessage(byte[] content, Connection connection) {
        super(new Packet(Command.PUSH, genSessionId()), connection);
        this.content = content;
    }

    public PushMessage(Packet packet, Connection connection) {
        super(packet, connection);
    }

    @Override
    public void decode(byte[] body) {
        content = body;
    }

    @Override
    public byte[] encode() {
        return content;
    }

    public boolean autoAck() {
        return packet.hasFlag(Packet.FLAG_AUTO_ACK);
    }

    public boolean bizAck() {
        return packet.hasFlag(Packet.FLAG_BIZ_ACK);
    }

    public PushMessage addFlag(byte flag) {
        packet.addFlag(flag);
        return this;
    }

    @Override
    public String toString() {
        return "PushMessage{" +
                "content='" + content.length + '\'' +
                '}';
    }
}
