package com.dark.video_delivery.service.impl;

import java.util.UUID;

import com.dark.video_delivery.dto.Metadata;
import com.dark.video_delivery.dto.MetadataWithChunk;
import com.dark.video_delivery.service.DeliveryService;
import com.dark.video_delivery.service.PreviewStorageService;
import com.dark.video_delivery.service.ThumbnailStorageService;
import com.dark.video_delivery.service.VideoStorageService;
import com.dark.video_delivery.util.ChunkReader;
import com.dark.video_delivery.util.Range;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Value("${services.video_service.url}")
    private String videoServiceUrl;

    private final VideoStorageService videoStorageService;
    private final PreviewStorageService previewStorageService;
    private final ThumbnailStorageService thumbnailStorageService;
    private final RestTemplate restTemplete;

    @Override
    public MetadataWithChunk fetchVideoChunk(UUID uuid, Range range) {
        // FIX: DOS alert. We're blasting the video service with this request. need to
        // cache the metadata to reuse it somehow and not send useless requests to video
        // service.
        String url = videoServiceUrl + "/video/" + uuid + "/metadata";
        Metadata metadata = restTemplete.getForObject(url, Metadata.class);
        return new MetadataWithChunk(metadata.name(),
                metadata.size(),
                MediaType.parseMediaType(metadata.contentType()),
                ChunkReader.read(metadata.name(), range, metadata.size(), videoStorageService));

    }

    @Override
    public MetadataWithChunk fetchPreviewChunk(UUID uuid, Range range) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MetadataWithChunk fetchThumbnail(UUID uuid) {
        // TODO Auto-generated method stub
        return null;
    }

}
