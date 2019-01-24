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
import com.aismono.mpush.api.protocol.Packet;
import com.aismono.mpush.util.ByteBuf;
import com.aismono.mpush.util.MPUtils;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ohun on 2016/2/15.
 *
 * @author ohun@live.cn (夜色)
 */
public final class HttpResponseMessage extends ByteBufMessage {
    public int statusCode;
    public String reasonPhrase;
    public Map<String, String> headers;
    public byte[] body;

    public HttpResponseMessage(Packet message, Connection connection) {
        super(message, connection);
    }

    @Override
    public void decode(ByteBuffer body) {
        statusCode = decodeInt(body);
        reasonPhrase = decodeString(body);
        headers = MPUtils.headerFromString(decodeString(body));
        this.body = decodeBytes(body);
    }

    @Override
    public void encode(ByteBuf body) {
        encodeInt(body, statusCode);
        encodeString(body, reasonPhrase);
        encodeString(body, MPUtils.headerToString(headers));
        encodeBytes(body, this.body);
    }

    public static HttpResponseMessage from(HttpRequestMessage src) {
        return new HttpResponseMessage(src.createResponse(), src.connection);
    }

    public HttpResponseMessage setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public HttpResponseMessage setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
        return this;
    }

    public HttpResponseMessage addHeader(String name, String value) {
        if (headers == null) headers = new HashMap<>();
        this.headers.put(name, value);
        return this;
    }
}
