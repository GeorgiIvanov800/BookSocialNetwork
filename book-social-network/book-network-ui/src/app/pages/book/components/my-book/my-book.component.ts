import {Component, OnInit} from '@angular/core';
import {BookCardComponent} from '../book-card/book-card.component';
import {NgForOf, NgIf} from '@angular/common';
import {PageResponseBookResponse} from '../../../../services/models/page-response-book-response';
import {BookService} from '../../../../services/services/book.service';
import {Router, RouterLink} from '@angular/router';
import {BookResponse} from '../../../../services/models/book-response';

@Component({
  selector: 'app-my-book',
  imports: [
    BookCardComponent,
    NgForOf,
    NgIf,
    RouterLink
  ],
  templateUrl: './my-book.component.html',
  styleUrl: './my-book.component.scss'
})
export class MyBookComponent implements OnInit {
  bookResponse: PageResponseBookResponse = {};

  page: number = 0;
  size: number = 2;

  constructor(private bookService: BookService, private router: Router) {
  }

  ngOnInit(): void {
    this.findAllBooksByOwner();
  }

  private findAllBooksByOwner() {
    this.bookService.findAllBooksByOwner({
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
    this.findAllBooksByOwner();
  }

  GoToPreviousPage() {
    this.page--;
    this.findAllBooksByOwner();
  }

  GoToPage(page: number) {
    this.page = page;
    this.findAllBooksByOwner();
  }

  goToNextPage() {
    this.page++;
    this.findAllBooksByOwner();
  }

  GoToLastPage() {
    this.page = this.bookResponse.totalPages as number - 1;
    this.findAllBooksByOwner();
  }

  get isLastPage(): boolean {
    return this.page == this.bookResponse.totalPages as number - 1;
  }

  archiveBook(book: BookResponse) {
      this.bookService.updateArchiveStatus({
        'book-id': book.id as number,
      }).subscribe({
        next: () => {
          book.archived = !book.archived;
        }
      })
  }

  shareBook(book: BookResponse) {
    this.bookService.updateShareableStatus({
      'book-id': book.id as number,
    })
      .subscribe( {
        next: () => {
          book.shareable = !book.shareable;
        }
      })
  }

  editBook(book: BookResponse) {
      this.router.navigate(['/books/manage/', book.id]);
  }
}
