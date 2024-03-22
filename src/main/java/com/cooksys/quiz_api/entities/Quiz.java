package com.cooksys.quiz_api.entities;

import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Quiz {
//    // NOTE: Code Snippet that makes each table use its own id generation sequence, saved here for reference
//    @Id
//    @SequenceGenerator(
//      name = "quiz_sequence",
//      sequenceName = "quiz_sequence",
//      allocationSize = 1
//    )
//    @GeneratedValue(
//      strategy = GenerationType.SEQUENCE,
//      generator = "quiz_sequence"
//    )
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "quiz")
    private List<Question> questions;

    private boolean isDeleted;

}
