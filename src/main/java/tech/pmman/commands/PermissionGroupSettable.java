package tech.pmman.commands;

public interface PermissionGroupSettable {
    void resetPermissionGroups(String ...groups);

    String getPermissionStr();
}
