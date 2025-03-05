package com.georgi.book.feedback;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FeedbackService {


    public Integer save(FeedbackRequest request, Authentication connectedUser) {
        return null;
    }
}
