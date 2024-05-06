package com.alioth.server.common.image.controller;

import com.alioth.server.common.image.dto.ImageResDto;
import com.alioth.server.common.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/server/api/image")
public class ImageUploadController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ImageResDto> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        String imageUrl = imageService.uploadImage(file);
        ImageResDto response = new ImageResDto(imageUrl);
        return ResponseEntity.ok(response);
    }
}
