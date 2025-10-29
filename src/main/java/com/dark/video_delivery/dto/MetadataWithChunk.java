package com.dark.video_delivery.dto;

import org.springframework.http.MediaType;

public record MetadataWithChunk(
        String name,
        long size,
        MediaType contentType,
        byte[] chunk) {
}
