package com.kiyata.ubg_auth.misc;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {

    public static byte[] getDefaultProfilePicture() {
        try {
            ClassPathResource imgFile = new ClassPathResource("default-pfp.png");
            InputStream inputStream = imgFile.getInputStream();
            return StreamUtils.copyToByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load default profile picture", e);
        }
    }
}
