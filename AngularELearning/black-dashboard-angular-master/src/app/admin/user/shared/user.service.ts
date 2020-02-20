import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { User } from './user.model';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserService {
    private apiUrl = environment.baseUrl;

    constructor(private http: HttpClient) {}

    public saveOrEditUser(vm: User): Observable<any> {
      const url = `${this.apiUrl}/user/save`;
      return this.http.post(url, JSON.stringify(vm), {responseType: 'text'})
        .pipe(
          map(response => response),
          catchError(this.handleError)
        );
    }

  public getUserViewModel(): Observable<any> {
    const url = `${this.apiUrl}/user`;
    return this.http.get(url)
      .pipe(
        map(result => {
          return this.mapResult(result);
        }),
        catchError(this.handleError)
      );
  }

  public getUserById(userId: number): Observable<any> {
    const url = `${this.apiUrl}/user/${userId}`;
    return this.http.get(url)
      .pipe(
        map(result => {
          return this.mapResult(result);
        }),
        catchError(this.handleError)
      );
  }

  public getAll(): Observable<any> {
    const url = `${this.apiUrl}/user/all`;
    return this.http.get(url)
      .pipe(
        map(result => result),
        catchError(this.handleError)
      );
  }

  public deleteUser(userId: number): Observable<any> {
    const url = `${this.apiUrl}/user/delete/${userId}`;
    return this.http.delete(url, {responseType: 'text'})
      .pipe(
        map(result => {
          return result;
        }),
        catchError(this.handleError)
      );
  }

    public mapResult(response: any): User {
        return new User(
            response.id,
            response.name,
            response.username,
            response.password,
            response.passwordConfirm,
            response.admin,
            response.professor,
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
}


