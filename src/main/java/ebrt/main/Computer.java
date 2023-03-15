package ebrt.main;

import java.nio.file.Paths;

public class Computer {

    public static int renderThreadCount() {
        return Runtime.getRuntime().availableProcessors() - 1;
    }

    public static String desktopFile(String name) {
        return Paths.get(System.getProperty("user.home"), "Desktop", name).toString();
    }
}
