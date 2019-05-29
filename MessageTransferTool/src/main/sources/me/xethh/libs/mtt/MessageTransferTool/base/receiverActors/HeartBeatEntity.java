package me.xethh.libs.mtt.MessageTransferTool.base.receiverActors;

import java.util.Date;
import java.util.Objects;

public class HeartBeatEntity{
    private String host;
    private String port;
    private Date date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeartBeatEntity that = (HeartBeatEntity) o;
        return Objects.equals(host, that.host) &&
                Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }

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
        return "HeartBeatEntity{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", date=" + date +
                '}';
    }
}

