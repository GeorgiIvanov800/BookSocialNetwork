package com.georgi.book.book;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    public Integer save(@Valid BookRequest request, Authentication connectedUser) {
        return null;
    }
}
