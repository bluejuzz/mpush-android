package com.mpush.demo;

/**
 * @author dinglaihong
 * @company {@see <a>http://www.aismono.com</a>}
 * @email 18279727279@163.com
 * @date 2019/01/23 10:17
 * @des
 */
public class TextMessage {
    private String text;

    public TextMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "text='" + text + '\'' +
                '}';
    }
}
