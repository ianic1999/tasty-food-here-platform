package com.example.tfhbackend.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Configuration {

    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(
                "5ZK2QLFUXWEUUJBJ2JIB",
                "68/l0aw4ig1sgQzz/6rbg4wPz32HiUu1D5JuuY909Nk"
        );
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        "fra1.digitaloceanspaces.com",
                        "fra1"
                ))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
