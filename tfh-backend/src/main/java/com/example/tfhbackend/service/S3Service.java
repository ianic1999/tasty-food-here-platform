package com.example.tfhbackend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {

    String save(MultipartFile file, String folder, String key) throws IOException;
    void delete(String key);
}
