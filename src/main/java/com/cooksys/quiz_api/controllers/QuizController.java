package com.cooksys.quiz_api.controllers;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<QuizResponseDto> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuizResponseDto createQuiz(@RequestBody QuizRequestDto quizRequestDto) {
        return quizService.createQuiz(quizRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public QuizResponseDto deleteQuiz(@PathVariable Long id) {
        return quizService.deleteQuiz(id);
    }

    @PatchMapping("/{id}/rename/{newName}")
    @ResponseStatus(HttpStatus.OK)
    public QuizResponseDto renameQuiz(@PathVariable("id") Long id, @PathVariable("newName") String name) {
        return quizService.renameQuiz(id, name);
    }

    @GetMapping("/{id}/random")
    @ResponseStatus(HttpStatus.OK)
    public QuestionResponseDto getRandomQuestion(@PathVariable Long id) {
        return quizService.getRandomQuestion(id);
    }

    @PatchMapping("/{id}/add")
    @ResponseStatus(HttpStatus.OK)
    public QuizResponseDto addQuestionToQuiz(@PathVariable Long id, @RequestBody QuestionRequestDto questionRequestDto) {
        return quizService.addQuestionToQuiz(id, questionRequestDto);
    }

    @DeleteMapping("/{id}/delete/{questionID}")
    @ResponseStatus(HttpStatus.OK)
    public QuestionResponseDto deleteQuestion(@PathVariable("id") Long id, @PathVariable("questionID") Long questionID) {
        return quizService.deleteQuestion(id, questionID);
    }


    @PostMapping("/{id}/{other}")
    @ResponseStatus(HttpStatus.CREATED)
    public QuizResponseDto otherEndpoint(@PathVariable Long id, @PathVariable String other) {
        return quizService.otherEndpoint(id, other);
    }
}
