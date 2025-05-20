package com.base.web.util;

import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.core.io.UrlResource;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * 拷贝自：{@link ImportCandidates#load(Class, ClassLoader)}
 *
 * @author suyh
 * @since 2025-05-16
 */
public class ErrorCodeLoader implements Iterable<String> {
    // suyh - 仅修改了这里
    // private static final String LOCATION = "META-INF/spring/%s.imports";
    private static final String LOCATION = "META-INF/services/%s";

    private static final String COMMENT_START = "#";

    private final List<String> candidates;

    private ErrorCodeLoader(List<String> candidates) {
        Assert.notNull(candidates, "'candidates' must not be null");
        this.candidates = Collections.unmodifiableList(candidates);
    }

    @Override
    public Iterator<String> iterator() {
        return this.candidates.iterator();
    }

    /**
     * Loads the names of import candidates from the classpath.
     *
     * The names of the import candidates are stored in files named
     * {@code META-INF/spring/full-qualified-annotation-name.imports} on the classpath.
     * Every line contains the full qualified name of the candidate class. Comments are
     * supported using the # character.
     * @param annotation annotation to load
     * @param classLoader class loader to use for loading
     * @return list of names of annotated classes
     */
    public static ErrorCodeLoader load(Class<?> annotation, ClassLoader classLoader) {
        Assert.notNull(annotation, "'annotation' must not be null");
        ClassLoader classLoaderToUse = decideClassloader(classLoader);
        String location = String.format(LOCATION, annotation.getName());
        Enumeration<URL> urls = findUrlsInClasspath(classLoaderToUse, location);
        List<String> autoConfigurations = new ArrayList<>();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            autoConfigurations.addAll(readAutoConfigurations(url));
        }
        return new ErrorCodeLoader(autoConfigurations);
    }

    private static ClassLoader decideClassloader(ClassLoader classLoader) {
        if (classLoader == null) {
            return ErrorCodeLoader.class.getClassLoader();
        }
        return classLoader;
    }

    private static Enumeration<URL> findUrlsInClasspath(ClassLoader classLoader, String location) {
        try {
            return classLoader.getResources(location);
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Failed to load autoconfigurations from location [" + location + "]",
                    ex);
        }
    }

    private static List<String> readAutoConfigurations(URL url) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new UrlResource(url).getInputStream(), StandardCharsets.UTF_8))) {
            List<String> autoConfigurations = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                line = stripComment(line);
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                autoConfigurations.add(line);
            }
            return autoConfigurations;
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Unable to load autoconfigurations from location [" + url + "]", ex);
        }
    }

    private static String stripComment(String line) {
        int commentStart = line.indexOf(COMMENT_START);
        if (commentStart == -1) {
            return line;
        }
        return line.substring(0, commentStart);
    }
}
