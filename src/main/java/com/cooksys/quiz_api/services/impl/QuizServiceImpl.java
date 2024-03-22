package com.cooksys.quiz_api.services.impl;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Question;
import com.cooksys.quiz_api.entities.Quiz;
import com.cooksys.quiz_api.exceptions.BadRequestException;
import com.cooksys.quiz_api.exceptions.DeletedAlreadyException;
import com.cooksys.quiz_api.exceptions.NotFoundException;
import com.cooksys.quiz_api.mappers.QuizMapper;
import com.cooksys.quiz_api.repositories.QuizRepository;
import com.cooksys.quiz_api.services.QuestionService;
import com.cooksys.quiz_api.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper;
    private final QuestionService questionService;

    private static <T> void validateNonNullInput(T input, String inputName){
        if (input == null) {
            throw new BadRequestException("Bad Request: Null input --> Invalid " + inputName);
        }
    }

    private static <T> void validateEntityExistsInDB(T input, String inputName, T inputId){
        if (input == null) {
            throw new NotFoundException("Not Found: Database does not contain a " + inputName + " with id: " + inputId);
        }
    }

    private static void validateNotDeletedAlready(Question question){
        if (question.isDeleted()) {
            // This shouldn't be reachable due to derived query service call to get question, but adding it here to double-check
            throw new DeletedAlreadyException("Deleted Already: Question with id: '" + question.getId() + "' has been deleted");
        }
    }

    private static void validateNotDeletedAlready(Quiz quiz){
        if (quiz.isDeleted()) {
            // This shouldn't be reachable due to derived query service call to get quiz, but adding it here to double-check
            throw new DeletedAlreadyException("Deleted Already: Quiz with id: '" + quiz.getId() + "' has been deleted");
        }
    }

    private static void validateQuiz(Quiz quiz, Long id) {
        validateEntityExistsInDB(quiz, "quiz", id);
        validateNotDeletedAlready(quiz);
    }

    private static void validateQuestion(Question question, Long id) {
        validateEntityExistsInDB(question, "question", id);
        validateNotDeletedAlready(question);
    }

    @Override
    public Quiz getQuizById(Long id) {
        // Validate input
        validateNonNullInput(id, "quiz id");

        // Retrieve active (not deleted) quiz from DB
        Quiz quiz = quizRepository.findByIdAndIsDeletedFalse(id);

        // Ensure quiz exists in DB and is not already deleted
        validateQuiz(quiz, id);

        // Overwrite quiz questions to include only active (not deleted) questions retrieved from DB
        quiz.setQuestions(questionService.getQuestionsByQuizId(id));

        return quiz;
    }

    @Override
    public List<QuizResponseDto> getAllQuizzes() {
        // Retrieve all active (not deleted) quizzes from DB
        List<Quiz> quizzesActive = quizRepository.findAllByIsDeletedFalse();

        for (Quiz quiz : quizzesActive) {
            // Overwrite quiz questions to include only active (not deleted) questions retrieved from DB
            quiz.setQuestions(questionService.getQuestionsByQuizId(quiz.getId()));
        }

        return quizMapper.entitiesToResponseDtos(quizzesActive);
    }

    @Override
    public QuizResponseDto createQuiz(QuizRequestDto quizRequestDto) {
        validateNonNullInput(quizRequestDto, "quiz request");

        // Convert quiz request to quiz entity, then create quiz in DB
        Quiz quiz = quizRepository.saveAndFlush(quizMapper.requestDtoToEntity(quizRequestDto));

        for (Question question : quiz.getQuestions()) {
            // Create each question in DB
            questionService.createQuestion(question, quiz);
        }

        return quizMapper.entityToResponseDto(quiz);
    }

    @Override
    public QuizResponseDto deleteQuiz(Long id) {
        // Retrieve quiz to delete from DB
        Quiz quizToDelete = getQuizById(id);

        // Ensure quiz exists in DB and is not already deleted
        validateQuiz(quizToDelete, id);

        for (Question question : quizToDelete.getQuestions()) {
            // Delete each question in DB
            questionService.deleteQuestion(question);
        }
        // Delete quiz by setting deleted boolean flag to true
        quizToDelete.setDeleted(true);

        // Write changes to DB before returning quiz
        return quizMapper.entityToResponseDto(quizRepository.saveAndFlush(quizToDelete));
    }

    @Override
    public QuizResponseDto renameQuiz(Long id, String name) {
        validateNonNullInput(name, "quiz name");

        // Retrieve quiz to delete from DB
        Quiz quiz = getQuizById(id);

        // Ensure quiz exists in DB and is not already deleted
        validateQuiz(quiz, id);

        // Update name
        quiz.setName(name);

        // Write changes to DB before returning quiz
        return quizMapper.entityToResponseDto(quizRepository.saveAndFlush(quiz));
    }

    @Override
    public QuestionResponseDto getRandomQuestion(Long id) {
        // Retrieve quiz to delete from DB
        Quiz quiz = getQuizById(id);

        // Ensure quiz exists in DB and is not already deleted
        validateQuiz(quiz, id);

        return questionService.getRandomQuestion(quiz);
    }

    @Override
    public QuizResponseDto addQuestionToQuiz(Long id, QuestionRequestDto questionRequestDto) {
        validateNonNullInput(questionRequestDto, "question request");

        // Retrieve quiz from DB
        Quiz quiz = getQuizById(id);

        // Ensure quiz exists in DB and is not already deleted
        validateQuiz(quiz, id);

        // Create question in DB and link it to quiz
        questionService.createQuestionWithRequstDto(questionRequestDto, quiz);

        // Retrieve quiz from DB again to reflect newly added question in responseDto
        return quizMapper.entityToResponseDto(getQuizById(id));
    }

    @Override
    public QuestionResponseDto deleteQuestion(Long id, Long questionID) {
        // Retrieve question to delete from DB
        Question question = questionService.getQuestionById(questionID);

        // Ensure question exists in DB and is not already deleted
        validateQuestion(question, questionID);

        // Ensure question comes from quiz id specified in request
        if (!Objects.equals(question.getQuiz().getId(), id)) {
            throw new BadRequestException("Bad Request: Quiz with id '" + id + "' does not contain a question with id: " + questionID);
        }

        // Delete question in DB, then return question responseDto
        return questionService.createQuestionResponseDto(questionService.deleteQuestion(question));

    }

    @Override
    public QuizResponseDto otherEndpoint(Long id, String other) {
        Optional<Quiz> quizToFind = quizRepository.findById(id);
        if (quizToFind.isEmpty()) {
            throw new NotFoundException("Not found: Could not locate quiz with id: " + id);
        }
        Quiz quiz = quizToFind.get();
        quiz.setName("other");
        return quizMapper.entityToResponseDto(quizRepository.saveAndFlush(quiz));
    }

}
