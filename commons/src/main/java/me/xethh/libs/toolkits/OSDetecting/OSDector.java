package me.xethh.libs.toolkits.OSDetecting;

import me.xethh.libs.toolkits.exceptions.CommonException;

public class OSDector {
    public static class OSDetectException extends CommonException{
        public OSDetectException(String message) {
            super(message);
        }
    }

    public static OS detect(){
        String operSys = System.getProperty("os.name").toLowerCase();
        if (operSys.contains("win")) {
            return OS.Windows;
        } else if (operSys.contains("nix") || operSys.contains("nux")
                || operSys.contains("aix")) {
            return OS.Linux;
        } else if (operSys.contains("mac")) {
            return OS.Mac;
        } else if (operSys.contains("sunos")) {
            return OS.Solaris;
        }
        else throw new OSDetectException("OS detection not working as expected");
    }
    public static void main(String[] args){
        System.out.println(OSDector.detect());
    }
}
