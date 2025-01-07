package com.example.workflowapi.validators;

import com.example.workflowapi.model.Comment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommentValidator implements Validator<Comment> {
    @Override
    public ValidationResult validate(Comment comment) {
        ValidationResult validationResult = new ValidationResult();
        List<String> errors = new ArrayList<>();

        if (!StringUtils.hasLength(comment.getContent())) {
            errors.add("Comment can't be empty.");
        }
        int MAX_CONTENT_LENGTH = 2000;
        if (comment.getContent().length() > MAX_CONTENT_LENGTH) {
            errors.add("Comment content exceeds maximum length.");
        }

        if (comment.getCreationDate() == null || comment.getCreationDate().isAfter(LocalDate.now())) {
            errors.add("Invalid creation date.");
        }
        validationResult.setValid(errors.isEmpty());
        validationResult.setErrors(errors);
        return validationResult;
    }
}
