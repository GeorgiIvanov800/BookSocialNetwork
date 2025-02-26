package com.georgi.book.book;

import com.georgi.book.common.PageResponse;
import com.georgi.book.user.User;
import com.georgi.book.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final UserRepository userRepository;
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    public Integer save(BookRequest request, Authentication connectedUser) {

        User user = ( (User) connectedUser.getPrincipal());
        Book book = bookMapper.toBook(request);
        book.setOwner(user);


        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow( () -> new EntityNotFoundException( "No book with ID: "  + bookId));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = ( (User) connectedUser.getPrincipal());

    }
}
