import { Component } from '@angular/core';
import {MenuComponent} from '../../menu/menu.component';
import {BookListComponent} from '../../book-list/book-list.component';
import {MyBookComponent} from '../../my-book/my-book.component';
import {RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-book-main',
  imports: [
    MenuComponent,
    BookListComponent,
    MyBookComponent,
    RouterOutlet
  ],
  templateUrl: './book-main.component.html',
  styleUrl: './book-main.component.scss'
})
export class BookMainComponent {

}
