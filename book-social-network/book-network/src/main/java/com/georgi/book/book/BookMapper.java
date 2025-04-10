package com.georgi.book.book;

import com.georgi.book.file.FileUtils;
import com.georgi.book.history.BookTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {


    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.id())
                .isbn(request.isbn())
                .title(request.title())
                .authorName(request.authorName())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .synopsis(book.getSynopsis())
                .isbn(book.getIsbn())
                .rate(book.getRate())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .owner(book.getOwner().fullName())
                .cover(book.getBookCover())
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {

            return BorrowedBookResponse.builder()
                    .id(history.getBook().getId())
                    .title(history.getBook().getTitle())
                    .authorName(history.getBook().getAuthorName())
                    .isbn(history.getBook().getIsbn())
                    .rate(history.getBook().getRate())
                    .returned(history.isReturned())
                    .returnApproved(history.isReturnApproved())
                    .build();
    }
}
