import { Injectable } from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  set token(token: string) {
    localStorage.setItem('token', token);
  }

  get token() {
    return localStorage.getItem('token') as string;
  }

  isTokenValid() {
    const token = localStorage.getItem('token');
    console.log(token);
    if (!token) {
      return false
    }

    const jwtHelper = new JwtHelperService();

    const isTokenExpired = jwtHelper.isTokenExpired(token);

    if (isTokenExpired) {
      localStorage.clear()
      return false
    }
    return true;
  }
}
