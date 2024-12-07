package com.kosa.moimeasy.membership.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    @Enumerated(EnumType.STRING)
    private CategoryType type;

    public enum CategoryType {
        TYPE1,
        TYPE2
    }
}