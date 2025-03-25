import {Routes} from '@angular/router';
import {LoginComponent} from './pages/login/login.component';
import {RegisterComponent} from './pages/register/register.component';
import {ActivateAccountComponent} from './pages/activate-account/activate-account.component';
import {BookMainComponent} from './pages/book/components/book-main/book-main/book-main.component';
import {BookListComponent} from './pages/book/components/book-list/book-list.component';
import {MyBookComponent} from './pages/book/components/my-book/my-book.component';

export const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'activate-account', component: ActivateAccountComponent},
  {
    path: '', component: BookMainComponent, children: [
      {path: 'books', component: BookListComponent},
      {path: 'my-books', component: MyBookComponent}
    ]
  },

];
