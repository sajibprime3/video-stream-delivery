package com.dark.video_delivery.service;

import java.util.UUID;

import com.dark.video_delivery.dto.MetadataWithChunk;
import com.dark.video_delivery.util.Range;

public interface DeliveryService {

    MetadataWithChunk fetchVideoChunk(UUID uuid, Range range);

    MetadataWithChunk fetchPreviewChunk(UUID uuid, Range range);

    MetadataWithChunk fetchThumbnail(UUID uuid);

}
