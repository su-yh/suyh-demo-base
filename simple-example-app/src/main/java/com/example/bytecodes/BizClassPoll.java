package com.example.bytecodes;

import javassist.ClassPool;
import javassist.LoaderClassPath;

/**
 * @author suyh
 * @since 2024-10-19
 */
public class BizClassPoll {
    static {
        init();
    }

    public static ClassPool getDefault() {
        return ClassPool.getDefault();
    }

    private static void init() {
        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        importPackages(classPool);
    }

    private static void importPackages(ClassPool classPool) {
        classPool.importPackage("com.mysql.cj.PreparedQuery");
    }
}
