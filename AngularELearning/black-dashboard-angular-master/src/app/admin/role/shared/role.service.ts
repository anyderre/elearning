import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { Rol } from './role.model';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
    private apiUrl = environment.baseUrl;

  constructor(private http: HttpClient) {}

  public saveOrEditRole(vm: Rol): Observable<any> {
    const url = `${this.apiUrl}/role/save`;

    return this.http.post(url, JSON.stringify(vm), {responseType: 'text'})
      .pipe(
        map(response => response),
        catchError(this.handleError)
      );
  }

  public getRoleViewModel(): Observable<any> {
    const url = `${this.apiUrl}/role`;
    return this.http.get(url)
      .pipe(
        map(result => {
          return this.mapResult(result);
        }),
        catchError(this.handleError)
      );
  }

  public getRoleById(roleId: number): Observable<any> {
    const url = `${this.apiUrl}/role/${roleId}`;
    return this.http.get(url)
      .pipe(
        map(result => {
          return this.mapResult(result);
        }),
        catchError(this.handleError)
      );
  }

  public getAll(): Observable<any> {
    const url = `${this.apiUrl}/role/all`;
    return this.http.get(url)
      .pipe(
        map(result => result),
        catchError(this.handleError)
      );
  }

  public getInfo(): Observable<any> {
    const url = `${this.apiUrl}/role/info`;
    return this.http.get(url)
      .pipe(
        map(result => result),
        catchError(this.handleError)
      );
  }

  public deleteRole(roleId: number): Observable<any> {
    const url = `${this.apiUrl}/role/delete/${roleId}`;
    return this.http.delete(url, {responseType: 'text'})
      .pipe(
        map(result => {
          return result;
        }),
        catchError(this.handleError)
      );
  }

    public mapResult(response: any): Rol {
      return new Rol(
          response.id,
          response.name,
          response.description,
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


