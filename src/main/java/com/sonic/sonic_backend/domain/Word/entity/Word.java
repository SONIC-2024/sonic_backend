package com.sonic.sonic_backend.domain.Word.entity;

import com.sonic.sonic_backend.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Word extends BaseEntity {
    @Id
    @Column(name = "word_id")
    private Long id;

    private String category;
    private String content;
    private String hand;
    @Column(name="object_url")
    private String objectUrl;

}
