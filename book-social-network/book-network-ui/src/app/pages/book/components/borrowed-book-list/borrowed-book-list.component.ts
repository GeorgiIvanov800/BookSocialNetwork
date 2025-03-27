import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {PageResponseBorrowedBookResponse} from '../../../../services/models/page-response-borrowed-book-response';
import {BorrowedBookResponse} from '../../../../services/models/borrowed-book-response';
import {BookService} from '../../../../services/services/book.service';
import {FeedbackRequest} from '../../../../services/models/feedback-request';
import {FormsModule} from '@angular/forms';
import {RatingComponent} from '../rating/rating.component';
import {FeedbackService} from '../../../../services/services/feedback.service';


@Component({
  selector: 'app-borrowed-book-list',
  imports: [
    NgForOf,
    NgIf,
    FormsModule,
    RatingComponent,
  ],
  templateUrl: './borrowed-book-list.component.html',
  styleUrl: './borrowed-book-list.component.scss'
})
export class BorrowedBookListComponent  implements OnInit {
  page = 0;
  size = 5;

  selectedBook: BorrowedBookResponse | null = null;
  feedBackRequest: FeedbackRequest = {bookId: 0, comment: '', note: 0};

  constructor(private bookService: BookService, private feedbackService: FeedbackService) {
  }

  ngOnInit(): void {
      this.findAllBorrowedBooks();
  }
  borrowedBooks: PageResponseBorrowedBookResponse = {};

  returnBorrowedBook(book: BorrowedBookResponse) {
      this.selectedBook = book;
      this.feedBackRequest.bookId = book.id as number;
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

  returnBook(withFeedback: boolean) {
    console.log("Clicked")
    if (!this.selectedBook) {
      console.warn('No book selected!');
      return;
    }

    this.bookService.returnBook({
        'book-id': this.selectedBook.id as number
      }).subscribe({
      next: () => {
       if (withFeedback) {
         this.giveFeedback();
       }
       this.selectedBook = null;
       this.findAllBorrowedBooks();
      }
    });
  }

  private giveFeedback() {
    this.feedbackService.saveFeedback({
      body: this.feedBackRequest
    })
      .subscribe({
        next: () => {}
      })
  }


}
