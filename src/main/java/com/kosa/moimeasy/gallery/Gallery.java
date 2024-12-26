package com.kosa.moimeasy.gallery;

import com.kosa.moimeasy.moeim.entity.Moeim;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Moeim moeimId;
    private String imageUrl;   // 이미지 경로 또는 URL
}
