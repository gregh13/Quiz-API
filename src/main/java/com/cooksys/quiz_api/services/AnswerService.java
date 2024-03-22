package com.cooksys.quiz_api.services;

import com.cooksys.quiz_api.entities.Answer;
import com.cooksys.quiz_api.entities.Question;

public interface AnswerService {

    void createAnswer(Answer answer, Question question);

    void deleteAnswer(Answer answer);
}
