package me.xethh.libs.toolkits.sysMeta;

import java.util.Objects;

public class ModuleMeta {
    private String moduleCode;
    private String moduleName;
    private String description;
    private SystemMeta systemMeta;

    public ModuleMeta() {
        this("","","",null);
    }

    public ModuleMeta(String moduleCode, String moduleName, SystemMeta systemMeta) {
        this(moduleCode, moduleName, "", systemMeta);
    }

    public ModuleMeta(String moduleCode, String moduleName, String description, SystemMeta systemMeta) {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.description = description;
        this.systemMeta = systemMeta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleMeta that = (ModuleMeta) o;
        return Objects.equals(moduleCode, that.moduleCode) &&
                Objects.equals(moduleName, that.moduleName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(systemMeta, that.systemMeta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleCode, moduleName, description, systemMeta);
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SystemMeta getSystemMeta() {
        return systemMeta;
    }

    public void setSystemMeta(SystemMeta systemMeta) {
        this.systemMeta = systemMeta;
    }
}
