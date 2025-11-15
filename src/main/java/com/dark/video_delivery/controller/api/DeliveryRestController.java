package com.dark.video_delivery.controller.api;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.dark.video_delivery.controller.HttpConstants;
import com.dark.video_delivery.dto.MetadataWithChunk;
import com.dark.video_delivery.service.DeliveryService;
import com.dark.video_delivery.util.Range;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/playback")
public class DeliveryRestController {

    private final DeliveryService deliveryService;

    @Value("${app.streaming.default-chunk-size}")
    public Integer defaultChunkSize;

    @GetMapping("/{uuid}")
    public ResponseEntity<byte[]> getVideoChunk(
            @RequestHeader(value = HttpHeaders.RANGE, required = false) String range,
            @PathVariable UUID uuid) {
        Range parsedRange = Range.parseHttpRangeString(range, defaultChunkSize);
        MetadataWithChunk chunkWithMetadata = deliveryService.fetchVideoChunk(uuid, parsedRange);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(chunkWithMetadata.contentType())
                .header(HttpHeaders.ACCEPT_RANGES, HttpConstants.ACCEPTS_RANGES_VALUE)
                .header(HttpHeaders.CONTENT_LENGTH, calculateContentLengthHeader(parsedRange, chunkWithMetadata.size()))
                .header(HttpHeaders.CONTENT_RANGE, constructContentRangeHeader(parsedRange, chunkWithMetadata.size()))
                .body(chunkWithMetadata.chunk());
    }

    @GetMapping("/{uuid}/preview")
    public ResponseEntity<byte[]> getPreviewChunk(
            @RequestHeader(value = HttpHeaders.RANGE, required = false) String range,
            @PathVariable UUID uuid) {
        Range parsedRange = Range.parseHttpRangeString(range, defaultChunkSize);
        MetadataWithChunk chunkWithMetadata = deliveryService.fetchPreviewChunk(uuid, parsedRange);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(chunkWithMetadata.contentType())
                .header(HttpHeaders.ACCEPT_RANGES, HttpConstants.ACCEPTS_RANGES_VALUE)
                .header(HttpHeaders.CONTENT_LENGTH, calculateContentLengthHeader(parsedRange, chunkWithMetadata.size()))
                .header(HttpHeaders.CONTENT_RANGE, constructContentRangeHeader(parsedRange, chunkWithMetadata.size()))
                .body(chunkWithMetadata.chunk());
    }

    @GetMapping("/{uuid}/thumbnail")
    public ResponseEntity<byte[]> getThumbnail(@PathVariable UUID uuid) {
        MetadataWithChunk fetchedChunkWithMetadata = deliveryService.fetchThumbnail(uuid);
        return ResponseEntity.ok()
                .contentType(fetchedChunkWithMetadata.contentType())
                .cacheControl(CacheControl.maxAge(10, TimeUnit.DAYS))
                .body(fetchedChunkWithMetadata.chunk());
    }

    private String calculateContentLengthHeader(Range range, long fileSize) {
        return String.valueOf(range.getRangeEnd(fileSize) - range.getRangeStart() + 1);
    }

    private String constructContentRangeHeader(Range range, long fileSize) {
        return "bytes " + range.getRangeStart() + "-" + range.getRangeEnd(fileSize) + "/" + fileSize;
    }

}
