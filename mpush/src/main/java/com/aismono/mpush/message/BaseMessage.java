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




import com.aismono.mpush.api.Message;
import com.aismono.mpush.api.connection.Connection;
import com.aismono.mpush.api.connection.SessionContext;
import com.aismono.mpush.api.protocol.Packet;
import com.aismono.mpush.client.ClientConfig;
import com.aismono.mpush.util.IOUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ohun on 2015/12/28.
 *
 * @author ohun@live.cn (夜色)
 */
public abstract class BaseMessage implements Message {
    public static final byte STATUS_DECODED = 1;
    public static final byte STATUS_ENCODED = 2;
    private static final AtomicInteger SID_SEQ = new AtomicInteger();
    protected final Packet packet;
    protected final Connection connection;
    protected byte status = 0;

    public BaseMessage(Packet packet, Connection connection) {
        this.packet = packet;
        this.connection = connection;
    }

    @Override
    public void decodeBody() {
        if ((status & STATUS_DECODED) != 0) {
            return;
        } else {
            status |= STATUS_DECODED;
        }

        if (packet.body != null && packet.body.length > 0) {
            //1.解密
            byte[] tmp = packet.body;
            if (packet.hasFlag(Packet.FLAG_CRYPTO)) {
                if (connection.getSessionContext().cipher != null) {
                    tmp = connection.getSessionContext().cipher.decrypt(tmp);
                }
            }

            //2.解压
            if (packet.hasFlag(Packet.FLAG_COMPRESS)) {
                tmp = IOUtils.uncompress(tmp);
            }

            if (tmp.length == 0) {
                throw new RuntimeException("message decode ex");
            }

            packet.body = tmp;
            decode(packet.body);
        }
    }

    @Override
    public void encodeBody() {
        if ((status & STATUS_ENCODED) != 0) {
            return;
        } else {
            status |= STATUS_ENCODED;
        }

        byte[] tmp = encode();
        if (tmp != null && tmp.length > 0) {
            //1.压缩
            if (tmp.length > ClientConfig.I.getCompressLimit()) {
                byte[] result = IOUtils.compress(tmp);
                if (result.length > 0) {
                    tmp = result;
                    packet.addFlag(Packet.FLAG_COMPRESS);
                }
            }

            //2.加密
            SessionContext context = connection.getSessionContext();
            if (context.cipher != null) {
                byte[] result = context.cipher.encrypt(tmp);
                if (result.length > 0) {
                    tmp = result;
                    packet.addFlag(Packet.FLAG_CRYPTO);
                }
            }
            packet.body = tmp;
        }
    }

    private void encodeBodyRaw() {
        if ((status & STATUS_ENCODED) != 0) {
            return;
        } else {
            status |= STATUS_ENCODED;
        }

        packet.body = encode();
    }

    protected abstract void decode(byte[] body);

    protected abstract byte[] encode();

    public Packet createResponse() {
        return new Packet(packet.cmd, packet.sessionId);
    }

    @Override
    public Packet getPacket() {
        return packet;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void send() {
        encodeBody();
        connection.send(packet);
    }

    @Override
    public void sendRaw() {
        encodeBodyRaw();
        connection.send(packet);
    }

    protected static int genSessionId() {
        return SID_SEQ.incrementAndGet();
    }

    public int getSessionId() {
        return packet.sessionId;
    }

    @Override
    public String toString() {
        return "BaseMessage{" +
                "packet=" + packet +
                ", connection=" + connection +
                '}';
    }
}
