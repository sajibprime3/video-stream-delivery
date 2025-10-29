package com.dark.video_delivery.service;

import com.dark.video_delivery.dto.MetadataWithChunk;
import com.dark.video_delivery.util.Range;

public interface DeliveryService {

    MetadataWithChunk fetchVideoChunk(long id, Range range);

    MetadataWithChunk fetchPreviewChunk(long id, Range range);

    MetadataWithChunk fetchThumbnail(long id);

}
