package com.example.tfhbackend.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageHandler {
    String save(MultipartFile file, String folder, String filename) throws IOException;
    void delete(String path) throws IOException;
}
