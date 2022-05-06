package com.example.tfhbackend.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.tfhbackend.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
class S3ServiceImpl implements S3Service {
    private final AmazonS3 amazonS3;
    private final String bucket = "tfh-iamges";

    @Override
    public String save(MultipartFile file, String folder, String key) throws IOException {
        final String path = folder + "/" + key;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getInputStream().available());
        metadata.setContentType(file.getContentType());
        amazonS3.putObject(new PutObjectRequest(bucket,
                                                path,
                                                file.getInputStream(),
                                                metadata)
                                   .withCannedAcl(CannedAccessControlList.PublicRead));
        return path;
    }

    @Override
    public void delete(String key) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, key));
    }
}
