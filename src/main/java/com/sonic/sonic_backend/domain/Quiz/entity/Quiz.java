package com.sonic.sonic_backend.domain.Quiz.entity;

import com.sonic.sonic_backend.domain.BaseEntity;
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
public class Quiz extends BaseEntity {
    @Id
    @Column(name="quiz_id")
    private Long id;

    private int level;
    private String content;
    /*
    detailed content
    level_1 : id array
    level_2 : object url
    level_3 : id
     */
    @Column(name="detailed_content")
    private String detailedContent;

    @Builder.Default
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SolvedQuiz> solvedQuiz = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StarredQuiz> starredQuiz = new ArrayList<>();
}
