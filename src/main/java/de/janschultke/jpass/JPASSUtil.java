package de.janschultke.jpass;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;

public class JPASSUtil {
    
    public static void printlnExit(int code, String str) {
        (code == 0? System.out : System.err).println(str);
        System.exit(code);
    }
    
    public static void printlnExit(int code, String str, Object... args) {
        printlnExit(code, String.format(str, args));
    }
    
    @NotNull
    public static String noSuffix(String path) {
        int lim = path.lastIndexOf('.');
        return lim < 0? path : path.substring(0, lim);
    }
    
    @Nullable
    public static String getSuffix(String path) {
        int start = path.lastIndexOf('.') + 1;
        return start == 0? null : path.substring(start);
    }
    
    @Nullable
    public static String getSuffix(Path path) {
        return getSuffix(path.toString());
    }
    
    @Nullable
    public static String getSuffix(File file) {
        return getSuffix(file.getName());
    }
    
}
