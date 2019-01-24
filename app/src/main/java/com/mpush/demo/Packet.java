package com.mpush.demo;

import com.aismono.mpush.message.BaseMessage;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dinglaihong
 * @company {@see <a>http://www.aismono.com</a>}
 * @email 18279727279@163.com
 * @date 2019/01/22 16:48
 * @des
 */
public class Packet {
    private String msgName;
    private String query;
    private String to;
    private String from;
    private String ver;
    private MessageBody body;

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public MessageBody getBody() {
        return body;
    }

    public void setBody(MessageBody body) {
        this.body = body;
    }

    public static class MessageBody {
        private static final AtomicInteger SID_SEQ = new AtomicInteger();
        private String fromId;
        private String toId;
        private String msglocalId;
        private int msgType;
        private Object content;

        public MessageBody(String fromId, String toId, int msgType, Object content) {
            this.fromId = fromId;
            this.toId = toId;
            this.msgType = msgType;
            this.content = content;
            this.msglocalId = String.valueOf(SID_SEQ.incrementAndGet());
        }

        public String getMsglocalId() {
            return msglocalId;
        }

        public void setMsglocalId(String msglocalId) {
            this.msglocalId = msglocalId;
        }

        public String getFromId() {
            return fromId;
        }

        public void setFromId(String fromId) {
            this.fromId = fromId;
        }

        public String getToId() {
            return toId;
        }

        public void setToId(String toId) {
            this.toId = toId;
        }

        public int getMsgType() {
            return msgType;
        }

        public void setMsgType(int msgType) {
            this.msgType = msgType;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "MessageBody{" +
                    "fromId='" + fromId + '\'' +
                    ", toId='" + toId + '\'' +
                    ", msgType=" + msgType +
                    ", content=" + content +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Packet{" +
                "msgName='" + msgName + '\'' +
                ", query='" + query + '\'' +
                ", to='" + to + '\'' +
                ", from='" + from + '\'' +
                ", ver='" + ver + '\'' +
                ", body=" + body +
                '}';
    }
}
