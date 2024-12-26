package com.kosa.moimeasy.gallery;

import com.kosa.moimeasy.gallery.dto.GalleryUploadRequest;
import com.kosa.moimeasy.gallery.dto.UploadFileInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gallery")
@RequiredArgsConstructor
public class GalleryController {
    private final GalleryService galleryService;

    // 갤러리 목록 조회
    @GetMapping
    public Page<Gallery> getGalleries(Long moeimId,Pageable pageable) {
        return galleryService.getGalleryList(moeimId, pageable);
    }

    // 갤러리 이미지 저장
    @PostMapping
    public List<Gallery> uploadImagesByUrl(@RequestBody GalleryUploadRequest request) {
        Long moeimId = request.getMoeimId();
        List<UploadFileInfo> files = request.getFiles();
        return galleryService.saveImageUrls(moeimId, files);
    }
}
