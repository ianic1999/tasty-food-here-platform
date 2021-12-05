package com.example.tfhbackend.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
class ImageHandlerImpl implements ImageHandler {

    @Value("${images.root}")
    private String root;

    @Override
    public String save(MultipartFile file, String folder, String filename) throws IOException {
        Path path = Paths.get(root.concat(folder).concat("/").concat(filename));
        Files.write(path, file.getBytes());
        return folder.concat("/").concat(filename);
    }

    @Override
    public void delete(String path) throws IOException {
        Files.deleteIfExists(Paths.get(root.concat(path)));
    }
}
