package com.cooksys.quiz_api.services;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.entities.Question;
import com.cooksys.quiz_api.entities.Quiz;

import java.util.List;

public interface QuestionService {
    Question getQuestionById(Long id);

    List<Question> getQuestionsByQuizId(Long quizId);

    QuestionResponseDto getRandomQuestion(Quiz quiz);

    void createQuestion(Question question, Quiz quiz);

    void createQuestionWithRequstDto(QuestionRequestDto questionRequestDto, Quiz quiz);

    QuestionResponseDto createQuestionResponseDto(Question question);

    Question deleteQuestion(Question question);

}
