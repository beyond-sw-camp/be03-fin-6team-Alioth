package com.alioth.server.common.image.service;

import com.alioth.server.common.aws.S3Service;
import com.alioth.server.common.image.domain.Image;
import com.alioth.server.common.image.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {

    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    public ImageService(S3Service s3Service, ImageRepository imageRepository) {
        this.s3Service = s3Service;
        this.imageRepository = imageRepository;
    }

    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("Upload attempt with empty file.");
        }

        // S3에 파일을 저장하고 URL을 반환합니다.
        String imageUrl = s3Service.saveFile(file);

        // 데이터베이스에 이미지 정보를 저장합니다.
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setImageUrl(imageUrl);
        imageRepository.save(image);

        return imageUrl;
    }
}
