package com.cooksys.quiz_api.services.impl;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.entities.Answer;
import com.cooksys.quiz_api.entities.Question;
import com.cooksys.quiz_api.entities.Quiz;
import com.cooksys.quiz_api.exceptions.BadRequestException;
import com.cooksys.quiz_api.mappers.QuestionMapper;
import com.cooksys.quiz_api.repositories.QuestionRepository;
import com.cooksys.quiz_api.services.AnswerService;
import com.cooksys.quiz_api.services.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final AnswerService answerService;

    private static <T> void validateInput(T input, String inputName) {
        if (input == null) {
            throw new BadRequestException("Bad Request: Null input --> Invalid " + inputName);
        }
    }

    private Integer getRandomIndex(int size) {
        // Create and return a random integer bounded from 0 (inclusive) to size (exclusive)
        return new Random().nextInt(size);
    }

    @Override
    public Question getQuestionById(Long id) {
        validateInput(id, "question id");

        // Retrieve active (not deleted) question from DB
        return questionRepository.findByIdAndIsDeletedFalse(id);
    }

    @Override
    public List<Question> getQuestionsByQuizId(Long quizId) {
        // Retrieve all active (not deleted) questions from DB
        return questionRepository.findByQuizIdAndIsDeletedFalse(quizId);
    }

    @Override
    public QuestionResponseDto getRandomQuestion(Quiz quiz) {
        // Get a random index to use with questions list
        int randomIndex = getRandomIndex(quiz.getQuestions().size());

        // Get questions from quiz, get random question from questions, return question responseDto
        return questionMapper.entityToResponseDto(quiz.getQuestions().get(randomIndex));
    }


    @Override
    public void createQuestion(Question question, Quiz quiz) {
        // Update question to be connected to its quiz
        question.setQuiz(quiz);

        // Write changes to DB
        question = questionRepository.saveAndFlush(question);

        for (Answer answer : question.getAnswers()) {
            // Create each answer in DB
            answerService.createAnswer(answer, question);
        }
    }


    public void createQuestionWithRequstDto(QuestionRequestDto questionRequestDto, Quiz quiz) {
        // Convert requestDto to question entity
        Question questionToCreate = questionMapper.requestDtoToEntity(questionRequestDto);

        // Ensure question entity was created properly
        validateInput(questionToCreate.getText(), "question text");
        validateInput(questionToCreate.getAnswers(), "question answers");
        if (questionToCreate.getAnswers().isEmpty()) {
            throw new BadRequestException("Bad Request: Each question must have at least one possible answer");
        }

        // Create question in DB
        createQuestion(questionToCreate, quiz);
    }

    @Override
    public QuestionResponseDto createQuestionResponseDto(Question question) {
        return questionMapper.entityToResponseDto(question);
    }

    @Override
    public Question deleteQuestion(Question question) {
        // Delete each answer in DB
        for (Answer answer : question.getAnswers()) {
            answerService.deleteAnswer(answer);
        }

        // Delete question by setting deleted boolean flag to true
        question.setDeleted(true);

        // Write changes to DB before returning question
        return questionRepository.saveAndFlush(question);
    }

}

