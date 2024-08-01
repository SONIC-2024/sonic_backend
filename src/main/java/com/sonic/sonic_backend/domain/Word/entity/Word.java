package com.sonic.sonic_backend.domain.Word.entity;

import com.sonic.sonic_backend.domain.BaseEntity;
import com.sonic.sonic_backend.domain.Quiz.entity.SolvedQuiz;
import com.sonic.sonic_backend.domain.Quiz.entity.StarredQuiz;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    @Column(name="object_url")
    private String objectUrl;

    @Builder.Default
    @OneToMany(mappedBy = "word", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StarredWord> starredWord = new ArrayList<>();
}
