package com.cooksys.quiz_api.services;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Quiz;

import java.util.List;

public interface QuizService {

    Quiz getQuizById(Long id);

    List<QuizResponseDto> getAllQuizzes();

    QuizResponseDto createQuiz(QuizRequestDto quizRequestDto);

    QuizResponseDto deleteQuiz(Long id);

    QuizResponseDto renameQuiz(Long id, String name);

    QuestionResponseDto getRandomQuestion(Long id);

    QuizResponseDto addQuestionToQuiz(Long id, QuestionRequestDto questionRequestDto);

    QuestionResponseDto deleteQuestion(Long id, Long questionID);

    QuizResponseDto otherEndpoint(Long id, String other);
}
