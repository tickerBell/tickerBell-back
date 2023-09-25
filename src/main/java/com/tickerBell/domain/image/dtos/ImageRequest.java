package com.tickerBell.domain.image.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
public class ImageRequest {
    @Schema(description = "썸네일 이미지")
    private MultipartFile thumbNailImg; // 썸네일 이미지
    @Schema(description = "썸네일 외 이미지")
    private List<MultipartFile> imageList;
    private String name;
}
