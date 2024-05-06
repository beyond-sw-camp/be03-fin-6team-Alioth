package com.alioth.server.common.image.repository;

import com.alioth.server.common.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
