package com.dch.data.filter.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ResourceUtils {

    private ResourceUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String readClasspathResource(String filePath) throws IOException {
        try (var inputStream = ResourceUtils.class.getClassLoader()
                .getResourceAsStream(filePath)) {
            return IOUtils.toString(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
        }
    }

}
