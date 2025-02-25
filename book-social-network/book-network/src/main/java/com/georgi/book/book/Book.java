package com.georgi.book.book;

import com.georgi.book.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


}
