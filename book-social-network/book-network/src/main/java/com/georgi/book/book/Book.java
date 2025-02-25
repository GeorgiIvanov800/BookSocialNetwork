package com.georgi.book.book;

import com.georgi.book.common.BaseEntity;
import com.georgi.book.feedback.Feedback;
import com.georgi.book.history.BookTransactionHistory;
import com.georgi.book.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "book")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book extends BaseEntity {

    private String title;

    private String authorName;

    private String isbn;

    private String publisher;

    private String synopsis;

    private String bookCover;

    private boolean archived;

    private boolean shareable;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;
}
