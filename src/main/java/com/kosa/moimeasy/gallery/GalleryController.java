package com.kosa.moimeasy.gallery;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GalleryController {
    private final GalleryService galleryService;



    @GetMapping
    public Page<Gallery> getGalleries(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return galleryService.findAll(pageable);
    }

}
