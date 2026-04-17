package com.example.offer.aws;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final String BUCKET_NAME = "offer-documents-sachin";

    public String uploadFile(String fileName, byte[] fileContent) {
        log.info("📤 Attempting S3 upload for file: {}", fileName);
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET_NAME)
                            .key(fileName)
                            .build(),
                    RequestBody.fromBytes(fileContent)
            );
            log.info("✅ Successfully stored evidence in S3: {}", fileName);
            return fileName;
        } catch (Exception e) {
            log.error("❌ S3 Upload Failed for [{}]: {}", fileName, e.getMessage());
            // Rethrowing as a RuntimeException triggers the Kafka @RetryableTopic logic
            throw new RuntimeException("CRITICAL_S3_FAILURE", e);
        }
    }


    public byte[] downloadFile(String fileName) {
        ResponseBytes<GetObjectResponse> response = s3Client.getObjectAsBytes(
                GetObjectRequest.builder()
                        .bucket(BUCKET_NAME)
                        .key(fileName)
                        .build()
        );
        return response.asByteArray();
    }
}
