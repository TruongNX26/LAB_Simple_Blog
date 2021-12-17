package edu.fpt.simple_blog.util;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class MyUtil {

    public static String encode(String originalString) {
        return Hashing.sha256()
                .hashString(originalString, StandardCharsets.UTF_8)
                .toString();
    }

    public static int getMaxPage(long count, int pageSize) {
        return (int) Math.ceil((double) count / pageSize);
    }
}
