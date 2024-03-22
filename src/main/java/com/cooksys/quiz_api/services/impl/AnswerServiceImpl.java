package com.cooksys.quiz_api.services.impl;

import com.cooksys.quiz_api.entities.Answer;
import com.cooksys.quiz_api.entities.Question;
import com.cooksys.quiz_api.exceptions.BadRequestException;
import com.cooksys.quiz_api.repositories.AnswerRepository;
import com.cooksys.quiz_api.services.AnswerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;

    private static <T> void validateInput(T input, String inputName) {
        if (input == null) {
            throw new BadRequestException("Bad Request: Null input --> Invalid " + inputName);
        }
    }

    @Override
    public void createAnswer(Answer answer, Question question) {
        // Ensure question entity was created properly
        validateInput(answer.getText(), "answer text");
        validateInput(answer.isCorrect(), "answer correctness");

        // Update answer to be connected to its question
        answer.setQuestion(question);

        // Write changes to DB
        answerRepository.saveAndFlush(answer);
    }

    @Override
    public void deleteAnswer(Answer answer) {
        // Delete answer by setting deleted boolean flag to true
        answer.setDeleted(true);

        // Write changes to DB
        answerRepository.saveAndFlush(answer);
    }

}
