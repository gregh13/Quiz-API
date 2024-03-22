package com.cooksys.quiz_api.entities;

import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Question {
//    // NOTE: Code Snippet that makes each table use its own id generation sequence, saved here for reference
//    @Id
//    @SequenceGenerator(
//      name = "question_sequence",
//      sequenceName = "question_sequence",
//      allocationSize = 1
//    )
//    @GeneratedValue(
//      strategy = GenerationType.SEQUENCE,
//      generator = "question_sequence"
//    )
    @Id
    @GeneratedValue
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;

    private boolean isDeleted;
}
