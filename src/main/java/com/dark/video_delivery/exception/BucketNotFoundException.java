package com.dark.video_delivery.exception;

import io.minio.errors.MinioException;

public class BucketNotFoundException extends MinioException {
    public BucketNotFoundException(String bucketname) {
        super("No such bucket Found by name: " + bucketname);
    }

}
