package com.georgi.book.book;

import com.georgi.book.common.PageResponse;
import com.georgi.book.exception.OperationNotPermittedException;
import com.georgi.book.file.FileStorageService;
import com.georgi.book.history.BookTransactionHistory;
import com.georgi.book.history.BookTransactionHistoryRepository;
import com.georgi.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.georgi.book.book.BookSpecification.*;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final FileStorageService fileStorageService;

    public Integer save(BookRequest request, Authentication connectedUser) {

        User user = getUser(connectedUser);

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
        User user = getUser(connectedUser);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());

        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = getUser(connectedUser);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<Book> books = bookRepository.findAll(withOwnerId(user.getId()), pageable);

        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = getUser(connectedUser);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository
                .findAllBorrowedBooks(pageable, user.getId());

        List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = getUser(connectedUser);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository
                .findAllReturnedBooks(pageable, user.getId());

        List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }


    public Integer updateSharableStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow( () -> new EntityNotFoundException( "No book with ID: "  + bookId));
        User user = getUser(connectedUser);

        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("Sorry cannot update books sharable status!");
        }

        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    private User getUser(Authentication connectedUser) {
        return ( (User) connectedUser.getPrincipal());
    }

    public Integer updateArchiveStatus(Integer bookId, Authentication connectedUser) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow( () -> new EntityNotFoundException( "No book with ID: "  + bookId));
        User user = getUser(connectedUser);

        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("Sorry cannot update others book archive status!");
        }

        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException( "No book with ID: "  + bookId));

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable!");
        }

        User user = getUser(connectedUser);

        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("Sorry you cannot borrowed your own book!");
        }

        final boolean isBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());

        if (isBorrowed) {
            throw new OperationNotPermittedException("Sorry you cannot borrow this book is already borrowed!");
        }

        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returnApproved(false)
                .returned(false)
                .build();
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer returnBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException( "No book with ID: "  + bookId));

        User user = getUser(connectedUser);

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable!");
        }

        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("Sorry you cannot return your own book!");
        }

        // TODO Double check if the Spring query works as expected if not use the self written query in the method => findByBookIdAndUserId
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository
                .findByBookIdAndUserIdAndReturnApprovedFalseAndReturnedFalse(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException(" You did not borrow this book"));
        bookTransactionHistory.setReturned(true);

        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer approveReturnBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException( "No book with ID: "  + bookId));

        User user = getUser(connectedUser);

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book cannot be borrowed since it is archived or not shareable!");
        }

        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("Sorry you cannot return your own book!");
        }

        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository
                .findByBookIdAndUserIdAndReturnApprovedFalseAndReturnedFalse(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("Sorry the book it's not returned"));

        bookTransactionHistory.setReturnApproved(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();

    }

    public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Integer bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException( "No book with ID: "  + bookId));
         User user = getUser(connectedUser);

         var bookCover = fileStorageService.saveFile(file, user.getId());

         book.setBookCover(bookCover);
         bookRepository.save(book);
    }
}
