import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {PageResponseBorrowedBookResponse} from '../../../../services/models/page-response-borrowed-book-response';
import {BorrowedBookResponse} from '../../../../services/models/borrowed-book-response';
import {BookService} from '../../../../services/services/book.service';

@Component({
  selector: 'app-borrowed-book-list',
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './borrowed-book-list.component.html',
  styleUrl: './borrowed-book-list.component.scss'
})
export class BorrowedBookListComponent  implements OnInit {
  page = 0;
  size = 5;
  selectedBook: BorrowedBookResponse = {};


  constructor(private bookService: BookService) {
  }

  ngOnInit(): void {
      this.findAllBorrowedBooks();
  }
  borrowedBooks: PageResponseBorrowedBookResponse = {};

  returnBorrowedBook(book: BorrowedBookResponse) {
      this.selectedBook = book;
  }

  private findAllBorrowedBooks() {
    this.bookService.findAllBorrowedBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: resp => {
        this.borrowedBooks = resp;
      }
    })
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBorrowedBooks();
  }

  GoToPreviousPage() {
    this.page--;
    this.findAllBorrowedBooks();
  }

  GoToPage(page: number) {
    this.page = page;
    this.findAllBorrowedBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllBorrowedBooks();
  }

  GoToLastPage() {
    this.page = this.borrowedBooks.totalPages as number - 1;
    this.findAllBorrowedBooks();
  }

  get isLastPage(): boolean {
    return this.page == this.borrowedBooks.totalPages as number - 1;
  }
}
