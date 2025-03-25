import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {BookRequest} from '../../../../services/models/book-request';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {BookService} from '../../../../services/services/book.service';
import {BookResponse} from '../../../../services/models/book-response';

@Component({
  selector: 'app-manage-book',
  imports: [
    NgIf,
    NgForOf,
    FormsModule,
    RouterLink
  ],
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss'
})
export class ManageBookComponent  implements OnInit{

  sampleCover: string = 'https://m.media-amazon.com/images/I/71SKgnTxoEL._UF1000,1000_QL80_.jpg';
  errorMsg: Array<string> = [];
  selectedPicture: string | undefined;
  selectedBookCover: any;
  bookRequest: BookRequest = {authorName: '', isbn: '', synopsis: '', title: ''};

  constructor(
    private bookService: BookService, private router: Router, private activatedRoute: ActivatedRoute,
  ) {
  }

  ngOnInit(): void {
    const bookId = this.activatedRoute.snapshot.params['id'];




        if (bookId) {
          this.bookService.findBookById({
            'book-id': bookId
          }).subscribe({
            next: (book: BookResponse) => {
              this.bookRequest = {
                id: book.id,
                title: book.title as string,
                authorName: book.authorName as string,
                isbn: book.isbn as string,
                synopsis: book.synopsis as string,
                shareable: book.shareable,
              }
            }
          });
        }
    }

  onFileSelect(event: any) {
    this.selectedBookCover = event.target.files[0];
    console.log(this.selectedBookCover);
    if (this.selectedBookCover) {
      const reader = new FileReader();
      reader.onload = () => {
        this.selectedPicture = reader.result as string;
      }
      reader.readAsDataURL(this.selectedBookCover);
    }
  }

  SaveBook() {
    this.bookService.saveBook({
      body: this.bookRequest,
    }).subscribe(
      {
        next: (bookId) => {
          this.bookService.uploadBookCoverPicture({
            'book-id': bookId,
            body: {
              file: this.selectedBookCover
            }
          }).subscribe({
            next: () => {
              this.router.navigate(['/books/my-books']);
            }
          })
        },
        error: (err) => {
          this.errorMsg = err.error.validationErrors;
        }
      }
    )
  }
}
