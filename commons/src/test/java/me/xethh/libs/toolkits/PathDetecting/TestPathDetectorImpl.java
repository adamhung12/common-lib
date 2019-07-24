package me.xethh.libs.toolkits.PathDetecting;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestPathDetectorImpl {
    @Test
    public void testWindowsLocalSunbasedPattern(){
        assertTrue(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN_SUN_BASED.matcher("a:/").matches());
        assertTrue(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN_SUN_BASED.matcher("a://").matches());
        assertTrue(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN_SUN_BASED.matcher("c://").matches());
        assertTrue(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN_SUN_BASED.matcher("z://").matches());
        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN_SUN_BASED.matcher("zx://").matches());
        assertTrue(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN_SUN_BASED.matcher("z://xxxx").matches());
        assertTrue(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN_SUN_BASED.matcher("z://xxxx/").matches());

        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN_SUN_BASED.matcher("z://xxxx/\\").matches());
        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN_SUN_BASED.matcher("z://xxxx/:").matches());
        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN_SUN_BASED.matcher("z://xxxx/*").matches());
        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN_SUN_BASED.matcher("z://xxxx/\"").matches());
        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN_SUN_BASED.matcher("z://xxxx/<").matches());
        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN_SUN_BASED.matcher("z://xxxx/>").matches());
        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN_SUN_BASED.matcher("z://xxxx/|").matches());
    }
    @Test
    public void testWindowsLocalPattern(){
        assertTrue(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN.matcher("a:\\").matches());
        assertTrue(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN.matcher("a:\\\\").matches());
        assertTrue(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN.matcher("c:\\\\").matches());
        assertTrue(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN.matcher("z:\\\\").matches());
        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN.matcher("zx:\\\\").matches());
        assertTrue(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN.matcher("z:\\\\xxxx").matches());
        assertTrue(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN.matcher("z:\\\\xxxx\\").matches());

        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN.matcher("z:\\\\xxxx\\/").matches());
        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN.matcher("z:\\\\xxxx\\:").matches());
        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN.matcher("z:\\\\xxxx\\*").matches());
        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN.matcher("z:\\\\xxxx\\\"").matches());
        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN.matcher("z:\\\\xxxx\\<").matches());
        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN.matcher("z:\\\\xxxx\\>").matches());
        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_LOCAL_PATH_PATTERN.matcher("z:\\\\xxxx\\|").matches());
    }
    @Test
    public void testWindowsNetworkPattern(){
        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_NETWORK_PATH_PATTERN.matcher("a:\\\\").matches());
        assertFalse(PathDetector.PathDetectorImpl.WINDOWS_NETWORK_PATH_PATTERN.matcher("\\\\xxx").matches());
        assertTrue(PathDetector.PathDetectorImpl.WINDOWS_NETWORK_PATH_PATTERN.matcher("\\\\xxx\\").matches());
        assertTrue(PathDetector.PathDetectorImpl.WINDOWS_NETWORK_PATH_PATTERN.matcher("\\\\xxx\\\\").matches());
        assertTrue(PathDetector.PathDetectorImpl.WINDOWS_NETWORK_PATH_PATTERN.matcher("\\\\xxx\\xxxx\\").matches());
    }
    @Test
    public void testLinuxLocalPattern(){
        assertTrue(PathDetector.PathDetectorImpl.LINUX_LOCAL_PATH_PATTERN.matcher("/").matches());
        assertTrue(PathDetector.PathDetectorImpl.LINUX_LOCAL_PATH_PATTERN.matcher("/xxx").matches());
        assertTrue(PathDetector.PathDetectorImpl.LINUX_LOCAL_PATH_PATTERN.matcher("/xxx/ss").matches());
    }
    @Test
    public void testLinuxNetworkPattern(){
        assertFalse(PathDetector.PathDetectorImpl.LINUX_NETWORK_PATH_PATTERN.matcher("//").matches());
        assertFalse(PathDetector.PathDetectorImpl.LINUX_NETWORK_PATH_PATTERN.matcher("//xxx").matches());
        assertTrue(PathDetector.PathDetectorImpl.LINUX_NETWORK_PATH_PATTERN.matcher("//xxx/ss").matches());
    }

    @Test
    public void testPathDetector(){
        PathDetector.PathDetectorImpl detector = null;

        detector = PathDetector.PathDetectorImpl.detect("c://users");
        assertEquals(detector.path(), "c://users");
        assertEquals(detector.type(), PathType.LocalPath);
        assertEquals(detector.sep(), PathDetector.PathDetectorImpl.Separator.Sun);
        assertEquals(detector.withSeparatorEnd(), false);

        detector = PathDetector.PathDetectorImpl.detect("c://users/");
        assertEquals(detector.path(), "c://users");
        assertEquals(detector.type(), PathType.LocalPath);
        assertEquals(detector.sep(), PathDetector.PathDetectorImpl.Separator.Sun);
        assertEquals(detector.withSeparatorEnd(), true);

        detector = PathDetector.PathDetectorImpl.detect("c:/users");
        assertEquals(detector.path(), "c:/users");
        assertEquals(detector.type(), PathType.LocalPath);
        assertEquals(detector.sep(), PathDetector.PathDetectorImpl.Separator.Sun);
        assertEquals(detector.withSeparatorEnd(), false);

        detector = PathDetector.PathDetectorImpl.detect("c:/users/");
        assertEquals(detector.path(), "c:/users");
        assertEquals(detector.type(), PathType.LocalPath);
        assertEquals(detector.sep(), PathDetector.PathDetectorImpl.Separator.Sun);
        assertEquals(detector.withSeparatorEnd(), true);

        detector = PathDetector.PathDetectorImpl.detect("c:\\users");
        assertEquals(detector.path(), "c:\\users");
        assertEquals(detector.type(), PathType.LocalPath);
        assertEquals(detector.sep(), PathDetector.PathDetectorImpl.Separator.Windows);
        assertEquals(detector.withSeparatorEnd(), false);

        detector = PathDetector.PathDetectorImpl.detect("c:\\users\\");
        assertEquals(detector.path(), "c:\\users");
        assertEquals(detector.type(), PathType.LocalPath);
        assertEquals(detector.sep(), PathDetector.PathDetectorImpl.Separator.Windows);
        assertEquals(detector.withSeparatorEnd(), true);

        detector = PathDetector.PathDetectorImpl.detect("c:\\\\users");
        assertEquals(detector.path(), "c:\\\\users");
        assertEquals(detector.type(), PathType.LocalPath);
        assertEquals(detector.sep(), PathDetector.PathDetectorImpl.Separator.Windows);
        assertEquals(detector.withSeparatorEnd(), false);

        detector = PathDetector.PathDetectorImpl.detect("c:\\\\users\\");
        assertEquals(detector.path(), "c:\\\\users");
        assertEquals(detector.type(), PathType.LocalPath);
        assertEquals(detector.sep(), PathDetector.PathDetectorImpl.Separator.Windows);
        assertEquals(detector.withSeparatorEnd(), true);

        detector = PathDetector.PathDetectorImpl.detect("\\\\host");
        assertEquals(detector.path(), "\\\\host");
        assertEquals(detector.type(), PathType.NetworkPath);
        assertEquals(detector.sep(), PathDetector.PathDetectorImpl.Separator.Windows);
        assertEquals(detector.withSeparatorEnd(), false);

        detector = PathDetector.PathDetectorImpl.detect("\\\\host\\");
        assertEquals(detector.path(), "\\\\host");
        assertEquals(detector.type(), PathType.NetworkPath);
        assertEquals(detector.sep(), PathDetector.PathDetectorImpl.Separator.Windows);
        assertEquals(detector.withSeparatorEnd(), true);

        detector = PathDetector.PathDetectorImpl.detect("//host");
        assertEquals(detector.path(), "//host");
        assertEquals(detector.type(), PathType.NetworkPath);
        assertEquals(detector.sep(), PathDetector.PathDetectorImpl.Separator.Sun);
        assertEquals(detector.withSeparatorEnd(), false);

        detector = PathDetector.PathDetectorImpl.detect("//host/");
        assertEquals(detector.path(), "//host");
        assertEquals(detector.type(), PathType.NetworkPath);
        assertEquals(detector.sep(), PathDetector.PathDetectorImpl.Separator.Sun);
        assertEquals(detector.withSeparatorEnd(), true);

        try {
            detector = PathDetector.PathDetectorImpl.detect("//host\\");
            assertTrue(false);
        }
        catch (PathDetector.PathDetectorSeparatorException ex){
            assertEquals("Path[//host\\] separator(\\ and /) count not as expected", ex.getMessage());
        }
        try {
            detector = PathDetector.PathDetectorImpl.detect("\\\\host/");
            assertTrue(false);
        }
        catch (PathDetector.PathDetectorSeparatorException ex){
            assertEquals("Path[\\\\host/] separator(\\ and /) count not as expected", ex.getMessage());
        }

    }

    @Test
    public void testFormTransformation(){
        PathDetector.PathDetectorImpl detector = null;
        detector = PathDetector.PathDetectorImpl.detect("c:\\user");
        assertEquals("c:/user",detector.linuxForm());
        assertEquals("c:\\user",detector.windowsForm());

        detector = PathDetector.PathDetectorImpl.detect("c:\\\\user\\");
        assertEquals("c://user/",detector.linuxForm());
        assertEquals("c:\\\\user\\",detector.windowsForm());

        detector = PathDetector.PathDetectorImpl.detect("/usr");
        assertEquals("/usr",detector.linuxForm());
        try {
            detector.windowsForm();
            assertTrue(false);
        }
        catch (PathDetector.PathDetectorNotSupportException ex){
            assertEquals("Path[/usr] not supporting windows form",ex.getMessage());
        }

        detector = PathDetector.PathDetectorImpl.detect("\\\\usr");
        assertEquals("//usr",detector.linuxForm());
        assertEquals("\\\\usr",detector.windowsForm());

        detector = PathDetector.PathDetectorImpl.detect("//usr");
        assertEquals("//usr",detector.linuxForm());
        assertEquals("\\\\usr",detector.windowsForm());
    }
}
