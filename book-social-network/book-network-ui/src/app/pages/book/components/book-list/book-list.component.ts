import {Component, OnInit} from '@angular/core';
import {BookService} from '../../../../services/services/book.service';
import {Router} from '@angular/router';
import {BookResponse} from '../../../../services/models/book-response';
import {PageResponseBookResponse} from '../../../../services/models/page-response-book-response';
import {NgForOf} from '@angular/common';
import {BookCardComponent} from '../book-card/book-card.component';

@Component({
  selector: 'app-book-list',
  imports: [
    NgForOf,
    BookCardComponent
  ],
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss'
})
export class BookListComponent implements OnInit {
  bookResponse: PageResponseBookResponse = {};

  page: number = 0;
  size: number = 2;

  constructor(private bookService: BookService, private router: Router) {
  }

  ngOnInit(): void {
        this.findAllBooks();
    }

  private findAllBooks() {
    this.bookService.findAllBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: books => {
        this.bookResponse = books;
      }
    });
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBooks();
  }

  GoToPreviousPage() {
    this.page--;
    this.findAllBooks();
  }

  GoToPage(page: number) {
    this.page = page;
    this.findAllBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllBooks();
  }

  GoToLastPage() {
    this.page = this.bookResponse.totalPages as number - 1;
    this.findAllBooks();
  }

  get isLastPage(): boolean {
      return this.page == this.bookResponse.totalPages as number - 1;
  }
}
