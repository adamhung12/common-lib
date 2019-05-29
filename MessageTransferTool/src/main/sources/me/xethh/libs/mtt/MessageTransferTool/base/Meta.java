package me.xethh.libs.mtt.MessageTransferTool.base;

import java.util.Date;

public class Meta {
    protected String host;
    protected String port;
    protected Date date;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", date=" + date +
                '}';
    }
}
