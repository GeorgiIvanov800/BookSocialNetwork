package com.georgi.book.feedback;

import com.georgi.book.book.Book;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    public Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .book(Book.builder().id(request.bookId()).build())
                .build();
    }
}
