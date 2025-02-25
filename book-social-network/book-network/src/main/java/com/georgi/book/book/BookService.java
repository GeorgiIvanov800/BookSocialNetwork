package com.georgi.book.book;

import com.georgi.book.user.User;
import com.georgi.book.user.UserRepository;
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

        User user = ( (User) connectedUser.getPrincipal() );
        Book book = bookMapper.toBook(request);
        book.setOwner(user);


        return bookRepository.save(book).getId();
    }
}
