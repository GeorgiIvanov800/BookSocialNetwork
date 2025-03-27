import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {BookService} from '../../../../services/services/book.service';
import {PageResponseBorrowedBookResponse} from '../../../../services/models/page-response-borrowed-book-response';

@Component({
  selector: 'app-returned-books',
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './returned-books.component.html',
  styleUrl: './returned-books.component.scss'
})
export class ReturnedBooksComponent implements OnInit {
  page = 0;
  size = 5;

  returnedBooks: PageResponseBorrowedBookResponse = {};

  constructor(private bookService: BookService) {
  }

  ngOnInit(): void {
    this.findAllReturnedBooks();
  }


  private findAllReturnedBooks() {
    this.bookService.findAllReturnedBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: resp => {
        this.returnedBooks = resp;
      }
    })
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllReturnedBooks();
  }

  GoToPreviousPage() {
    this.page--;
    this.findAllReturnedBooks();
  }

  GoToPage(page: number) {
    this.page = page;
    this.findAllReturnedBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllReturnedBooks();
  }

  GoToLastPage() {
    this.page = this.returnedBooks.totalPages as number - 1;
    this.findAllReturnedBooks();
  }

  get isLastPage(): boolean {
    return this.page == this.returnedBooks.totalPages as number - 1;
  }
}
