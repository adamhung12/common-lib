package me.xethh.libs.mtt.MessageTransferTool.client;

public class TelnetClientTask {
    private Integer periodInMinutes;
    private String template;
    private String targetType;
    private String host;
    private int port;
    private String url;

    public Integer getPeriodInMinutes() {
        return periodInMinutes;
    }

    public void setPeriodInMinutes(Integer periodInMinutes) {
        this.periodInMinutes = periodInMinutes;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
}
