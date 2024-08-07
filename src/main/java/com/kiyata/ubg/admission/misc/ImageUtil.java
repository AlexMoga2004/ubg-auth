package com.kiyata.ubg.admission.misc;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class ImageUtil {

    public static String getDefaultProfilePicture() {
        try {
            ClassPathResource imgFile = new ClassPathResource("default-pfp.png");
            InputStream inputStream = imgFile.getInputStream();
            byte[] imageBytes = StreamUtils.copyToByteArray(inputStream);
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load default profile picture", e);
        }
    }
}
