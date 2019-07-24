package me.xethh.libs.toolkits.PathDetecting;

import me.xethh.libs.toolkits.exceptions.CommonException;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.regex.Pattern;

public interface PathDetector{
    String path();
    PathDetectorImpl.Separator sep();
    PathType type();
    boolean withSeparatorEnd();

    String windowsForm();
    String linuxForm();
    File asFile();
    FileOutputStream outputStream();
    FileWriter utf8OutputStream();
    FileInputStream inputStream();


    public static class PathDetectorNotSupportException extends CommonException{
        public PathDetectorNotSupportException(String message) {
            super(message);
        }
    }
    public static class PathDetectorOutputStreamException extends CommonException{
        public PathDetectorOutputStreamException(String message) {
            super(message);
        }
    }
    public static class PathDetectorSeparatorException extends CommonException{
        public PathDetectorSeparatorException(String message) {
            super(message);
        }
    }
    public static class PathDetectorInitException extends CommonException{
        public PathDetectorInitException(String message) {
            super(message);
        }
    }
    public static class PathDetectorImpl implements PathDetector{

        private String path;
        private Separator sep;
        private PathType type;
        private boolean withSeparatorEnd;

        public String path() {
            return path;
        }

        public Separator sep() {
            return sep;
        }

        public PathType type() {
            return type;
        }

        public boolean withSeparatorEnd() {
            return withSeparatorEnd;
        }

        @Override
        public String windowsForm() {
            if(type==PathType.LocalPath &&  LINUX_LOCAL_PATH_PATTERN.matcher(linuxForm()).matches()){
                throw new PathDetectorNotSupportException(String.format("Path[%s] not supporting windows form", linuxForm()));
            }
            if(sep==Separator.Windows)
                return withSeparatorEnd?path+sep.sep():path;
            else
                return withSeparatorEnd?
                        path.replace(Separator.Sun.sep(),Separator.Windows.sep())+Separator.Windows.sep()
                        :
                        path.replace(Separator.Sun.sep(),Separator.Windows.sep());
        }

        @Override
        public String linuxForm() {
            if(sep==Separator.Sun)
                return withSeparatorEnd?path+sep():path;
            else
                return withSeparatorEnd?
                        path.replace(Separator.Windows.sep(),Separator.Sun.sep())+Separator.Sun.sep()
                        :
                        path.replace(Separator.Windows.sep(),Separator.Sun.sep());
        }

        @Override
        public File asFile() {
            return new File(linuxForm());
        }

        @Override
        public FileOutputStream outputStream() {
            try {
                return new FileOutputStream(linuxForm());
            } catch (FileNotFoundException e) {
                throw new PathDetectorOutputStreamException(String.format("File not found[%s]", linuxForm()));
            }
        }

        @Override
        public FileWriter utf8OutputStream() {
            try {
                return (FileWriter) new OutputStreamWriter(new FileOutputStream(linuxForm()),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new PathDetectorOutputStreamException(String.format("File[%s] encoding not support", linuxForm()));
            } catch (FileNotFoundException e) {
                throw new PathDetectorOutputStreamException(String.format("File not found[%s]", linuxForm()));
            }
        }

        @Override
        public FileInputStream inputStream() {
            try {
                return new FileInputStream(linuxForm());
            } catch (FileNotFoundException e) {
                throw new PathDetectorOutputStreamException(String.format("File not found[%s]", linuxForm()));
            }
        }

        public PathDetectorImpl(String path) {
            decomposeWindowsPath(path);
        }

        public static PathDetectorImpl detect(String path){
            return new PathDetectorImpl(path);

        }

        public enum Separator{
            Windows("\\"), Sun("/");
            private String sep;
            Separator(String sep){
                this.sep = sep;
            }
            public String sep(){
                return sep;
            }
            public String regex(){
                return this==Windows?sep+sep:sep;
            }
            public String duplicate(){
                return sep+sep;
            }
            public String quad(){
                return sep+sep+sep+sep;
            }
        }

        public static Pattern WINDOWS_LOCAL_PATH_PATTERN = Pattern.compile(String.format("^[a-zA-Z]:%s{1,2}[^/:\\*?\"\\<\\>\\|]*$", Separator.Windows.duplicate()));
        public static Pattern WINDOWS_LOCAL_PATH_PATTERN_SUN_BASED = Pattern.compile(String.format("^[a-zA-Z]:%s{1,2}[^\\\\:\\*?\"\\<\\>\\|]*$", Separator.Sun.sep()));
        public static Pattern WINDOWS_NETWORK_PATH_PATTERN = Pattern.compile(String.format("^%s\\w+.*$", Separator.Windows.quad()));
        public static Pattern LINUX_NETWORK_PATH_PATTERN = Pattern.compile(String.format("^%s\\w+.*$", Separator.Sun.duplicate()));
        public static Pattern LINUX_LOCAL_PATH_PATTERN = Pattern.compile(String.format("^%s.*$", Separator.Sun.sep()));

        private void decomposeWindowsPath(String path){
            if(StringUtils.countMatches(path, Separator.Windows.sep)>0 && StringUtils.countMatches(path, Separator.Sun.sep)>0)
                throw new PathDetectorSeparatorException(String.format("Path[%s] separator(\\ and /) count not as expected", path));
            else if(WINDOWS_LOCAL_PATH_PATTERN.matcher(path).matches()){
                if(StringUtils.countMatches(Separator.Sun.sep, path)>0)
                    throw new PathDetectorSeparatorException("Path[%s] separator expecting to be fully '/'");

                if(path.endsWith(Separator.Windows.sep())) {
                    this.path = path.substring(0, path.length() - 1);
                    this.withSeparatorEnd = true;
                }
                else
                    this.path = path;
                this.sep = Separator.Windows;
                this.type = PathType.LocalPath;
            }
            else if(WINDOWS_LOCAL_PATH_PATTERN_SUN_BASED.matcher(path).matches()){
                if(StringUtils.countMatches(Separator.Windows.sep, path)>0)
                    throw new PathDetectorSeparatorException("Path[%s] separator expecting to be fully '\\'");
                if(path.endsWith(Separator.Sun.sep())) {
                    this.path = path.substring(0, path.length() - 1);
                    this.withSeparatorEnd = true;
                }
                else
                    this.path = path;
                this.sep = Separator.Sun;
                this.type = PathType.LocalPath;
            }
            else if(WINDOWS_NETWORK_PATH_PATTERN.matcher(path).matches()){
                if(StringUtils.countMatches(Separator.Sun.sep, path)>0)
                    throw new PathDetectorSeparatorException("Path[%s] separator expecting to be fully '/'");
                if(path.endsWith(Separator.Windows.sep())) {
                    this.path = path.substring(0, path.length() - 1);
                    this.withSeparatorEnd = true;
                }
                else
                    this.path = path;
                this.sep = Separator.Windows;
                this.type = PathType.NetworkPath;
            }
            else if(LINUX_NETWORK_PATH_PATTERN.matcher(path).matches()){
                if(StringUtils.countMatches(Separator.Windows.sep, path)>0)
                    throw new PathDetectorSeparatorException("Path[%s] separator expecting to be fully '\\'");
                if(path.endsWith(Separator.Sun.sep())) {
                    this.path = path.substring(0, path.length() - 1);
                    this.withSeparatorEnd = true;
                }
                else
                    this.path = path;
                this.sep = Separator.Sun;
                this.type = PathType.NetworkPath;
            }
            else if(LINUX_LOCAL_PATH_PATTERN.matcher(path).matches()){
                if(path.endsWith(Separator.Sun.sep())) {
                    this.path = path.substring(0, path.length() - 1);
                    this.withSeparatorEnd = true;
                }
                else
                    this.path = path;
                this.sep = Separator.Sun;
                this.type = PathType.LocalPath;
            }
            else{
                int count1 = StringUtils.countMatches("/", path);
                int count2 = StringUtils.countMatches("\\", path);
                if(count1==0 && count2>0) {
                    this.sep = Separator.Windows;
                    this.withSeparatorEnd = true;
                }
                else if(count1>0 && count2==0) {
                    this.sep = Separator.Sun;
                    this.withSeparatorEnd = true;
                }
                else if(count1==0 && count2==0) {
                    this.sep = Separator.Sun;
                    this.withSeparatorEnd = false;
                }
                else
                    throw new PathDetectorSeparatorException("Path[%s] separator expecting to be fully '\\' or '/'");
                this.type = PathType.RelativePath;
                this.path = path;
            }
        }


    }
}
