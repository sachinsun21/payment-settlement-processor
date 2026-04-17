package com.example.offer.aws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client() {
        // This automatically looks for the AWS_ACCESS_KEY_ID
        // and AWS_SECRET_ACCESS_KEY you just set in IntelliJ!
        return S3Client.builder()
                .region(Region.AP_SOUTH_2) // Ensure this matches your IntelliJ variable
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
