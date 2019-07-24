package me.xethh.libs.toolkits.PathDetecting;

public class PathDetectorFactory {
    public static PathDetector get(String path){
        return new PathDetector.PathDetectorImpl(path);
    }
}
