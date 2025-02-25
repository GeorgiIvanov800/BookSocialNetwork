package com.georgi.book.history;

import com.georgi.book.book.Book;
import com.georgi.book.common.BaseEntity;
import com.georgi.book.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_transaction_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookTransactionHistory extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private boolean returned;
    private boolean returnApproved;
}
