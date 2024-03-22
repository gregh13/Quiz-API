package com.cooksys.quiz_api.entities;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Answer {
//    // NOTE: Code Snippet that makes each table use its own id generation sequence, saved here for reference
//    @Id
//    @SequenceGenerator(
//      name = "answer_sequence",
//      sequenceName = "answer_sequence",
//      allocationSize = 1
//    )
//    @GeneratedValue(
//      strategy = GenerationType.SEQUENCE,
//      generator = "answer_sequence"
//    )
    @Id
    @GeneratedValue
    private Long id;

    private String text;

    private boolean correct = false;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private boolean isDeleted;

}
