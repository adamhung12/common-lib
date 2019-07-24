package me.xethh.libs.toolkits.OSDetecting;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestOSDetector {
    @Test
    public void testDetect(){
        String windowStr = System.getProperty("os.name").toLowerCase();
        OS osNew = null;
        if (windowStr.contains("win")) {
            osNew = OS.Windows;
        } else if (windowStr.contains("nix") || windowStr.contains("nux")
                || windowStr.contains("aix")) {
            osNew =OS.Linux;
        } else if (windowStr.contains("mac")) {
            osNew =OS.Mac;
        } else if (windowStr.contains("sunos")) {
            osNew =OS.Solaris;
        }
        OS t = OSDector.detect();

        assertEquals(osNew, t);
    }

    public static void main(String[] args){
        new TestOSDetector().testDetect();
    }
}
