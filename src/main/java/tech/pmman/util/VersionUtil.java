package tech.pmman.util;

public class VersionUtil {
    public static String getVersion() {
        Package pkg = VersionUtil.class.getPackage();
        String version = pkg.getImplementationVersion();
        return version != null ? version : "DEV";
    }
}
