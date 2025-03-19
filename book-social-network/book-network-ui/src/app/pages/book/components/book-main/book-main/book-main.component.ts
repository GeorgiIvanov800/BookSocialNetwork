import { Component } from '@angular/core';
import {MenuComponent} from '../../menu/menu.component';
import {BookListComponent} from '../../book-list/book-list.component';

@Component({
  selector: 'app-book-main',
  imports: [
    MenuComponent,
    BookListComponent
  ],
  templateUrl: './book-main.component.html',
  styleUrl: './book-main.component.scss'
})
export class BookMainComponent {

}
