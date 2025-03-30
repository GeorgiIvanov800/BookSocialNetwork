import {Routes} from '@angular/router';
import {LoginComponent} from './pages/login/login.component';
import {RegisterComponent} from './pages/register/register.component';
import {ActivateAccountComponent} from './pages/activate-account/activate-account.component';
import {BookMainComponent} from './pages/book/components/book-main/book-main/book-main.component';
import {BookListComponent} from './pages/book/components/book-list/book-list.component';
import {MyBookComponent} from './pages/book/components/my-book/my-book.component';
import {ManageBookComponent} from './pages/book/components/manage-book/manage-book.component';
import {BorrowedBookListComponent} from './pages/book/components/borrowed-book-list/borrowed-book-list.component';
import {ReturnedBooksComponent} from './pages/book/components/returned-books/returned-books.component';
import {authGuard} from './services/guard/auth.guard';

export const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'activate-account', component: ActivateAccountComponent},
  {
    path: 'books', component: BookMainComponent,canActivate:[authGuard] ,children: [
      {path: '', component: BookListComponent},
      {path: 'my-books', component: MyBookComponent},
      {path:'manage', component: ManageBookComponent},
      {path:'manage/:id', component: ManageBookComponent},
      {path:'my-borrowed-books', component: BorrowedBookListComponent},
      {path:'my-returned-books', component: ReturnedBooksComponent}
    ]
  },

];
