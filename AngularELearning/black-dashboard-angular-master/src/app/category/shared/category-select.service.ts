import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CategorySelectService {
  private apiUrl = environment.baseUrl;

  constructor(private http: HttpClient) {}

  public getAllFiltered(filter: any = null): Observable<any> {
    // const url = `${this.apiUrl}/category/all/filtered`;
    const url = `${this.apiUrl}/category/all`;
// debugger
    return this.http.get(url, filter ? filter : null)
      .pipe(
        map(result => result),
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
}


