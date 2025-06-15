package com.jnu.capstone.util;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtil {
    public static MultipartFile urlToMultipart(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            try (InputStream inputStream = conn.getInputStream()) {
                byte[] bytes = inputStream.readAllBytes();

                return new MockMultipartFile(
                        "existingImages",
                        "image.jpg",
                        "image/jpeg",
                        bytes
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("이미지 다운로드 실패: " + e.getMessage(), e);
        }
    }
}
