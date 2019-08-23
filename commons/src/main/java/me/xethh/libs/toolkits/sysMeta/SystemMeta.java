package me.xethh.libs.toolkits.sysMeta;

import java.util.Objects;

public class SystemMeta {
    private String systemCode;
    private String systemName;
    private String description;

    public SystemMeta() {
        this("","","");
    }

    public SystemMeta(String systemCode, String systemName) {
        this(systemCode, systemName, "");
    }

    public SystemMeta(String systemCode, String systemName, String description) {
        this.systemCode = systemCode;
        this.systemName = systemName;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemMeta that = (SystemMeta) o;
        return Objects.equals(systemCode, that.systemCode) &&
                Objects.equals(systemName, that.systemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemCode, systemName);
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
