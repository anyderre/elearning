import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { map, catchError} from 'rxjs/operators';
import { environment } from '../../environments/environment';
import * as jwt_decode from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService  {
  private apiUrl = environment.apiUrl;
  constructor(public router: Router, private http: HttpClient) {
  }

  public login(username: string, password: string): Observable<any> {
    const url = `${this.apiUrl}/authenticate`;
    return this.http.post<{token: string}>(url, JSON.stringify({username , password}))
      .pipe(
        map(result => {
          localStorage.setItem('access_token', result.token);
        }),
        catchError(this.handleError)
      );
  }

  public handleError(error: any) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
        // client-side error
        errorMessage = `Error: ${error.error.message}`;
    } else {
        // server-side error
        errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    console.log(errorMessage);
    return throwError(errorMessage);
  }

  public getTokenExpirationDate(token: string): Date {
    const decoded = jwt_decode(token);

    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }

  public isTokenExpired(token?: string): boolean {
    if (!token) {
      token = this.getToken();
    }
    if (!token) {
      return true;
    }

    const date = this.getTokenExpirationDate(token);
    if (date === undefined) {
      return false;
    }
    return !(date.valueOf() > new Date().valueOf());
  }

  public logout() {
    localStorage.removeItem('access_token');
    this.router.navigate(['/dashboard']);
  }

  public getToken(): string {
    return localStorage.getItem('access_token');
  }

  public isLoggedIn(): boolean {
    return (localStorage.getItem('access_token') !== null);
  }

  public isLoggedOut(): boolean {
      return !this.isLoggedIn();
  }

}
