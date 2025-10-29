package com.dark.video_delivery.config;

import com.dark.video_delivery.exception.BucketNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;

@Configuration
public class MinioConfig {

    public static final String VIDEO_BUCKET_NAME = "videos";
    public static final String PREVIEW_BUCKET_NAME = "previews";
    public static final String THUMBNAIL_BUCKET_NAME = "thumbnails";

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.username}")
    private String minioUser;

    @Value("${minio.password}")
    private String minioPassword;

    @Bean
    public MinioClient minioClient() throws Exception {
        MinioClient client = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(minioUser, minioPassword)
                .build();
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(VIDEO_BUCKET_NAME).build())) {
            throw new BucketNotFoundException(VIDEO_BUCKET_NAME);
        }
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(PREVIEW_BUCKET_NAME).build())) {
            throw new BucketNotFoundException(PREVIEW_BUCKET_NAME);
        }
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(THUMBNAIL_BUCKET_NAME).build())) {
            throw new BucketNotFoundException(THUMBNAIL_BUCKET_NAME);
        }
        return client;
    }
}
