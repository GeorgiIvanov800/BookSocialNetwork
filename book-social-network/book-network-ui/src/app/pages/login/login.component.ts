import { Component } from '@angular/core';
import {AuthenticationRequest} from '../../services/models/authentication-request';
import {NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../services/services/authentication.service';

@Component({
  selector: 'app-login',
  imports: [
    NgIf,
    NgForOf,
    FormsModule,

  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  authRequest: AuthenticationRequest = {email: '', password: ''};
  errorMessage: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    // another service
  ) {
  }

  login() {
    this.errorMessage = [];
    this.authService.authenticate({
      body: this.authRequest
    }).subscribe({
      next: result => {
        // todo save the token
        this.router.navigate(['books']);
      },
      error: err => {
        // console.log(err);
        console.log(err.error);
        if (err.error.validationErrors) {
          this.errorMessage = err.error.validationErrors;
        } else {
          this.errorMessage.push(err.error.error);
        }
      }
    })
  }

  register() {
    this.router.navigate(['register']);
  }
}
