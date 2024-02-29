package web.utils;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 * 反射
 */
public class ReflectionUtil {
    /**
     * 获取 扫描对应包下的 扫描工具
     * @param packageName 全限定包名
     * @return Reflections
     */
    public static Reflections get(String packageName){
        return new Reflections(
                new ConfigurationBuilder()
                        .addUrls(ClasspathHelper.forPackage(packageName))
                        .setScanners(
                                new MethodAnnotationsScanner(),
                                new TypeAnnotationsScanner(),
                                new SubTypesScanner(false)
                        )
        );
    }
}
