import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Category } from './category.model';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
    private apiUrl = environment.baseUrl;

    constructor(private http: HttpClient) {}

  public saveOrEditCategory(vm: Category): Observable<any> {
    let url = `${this.apiUrl}/category/save` 
    return this.http.post(url, JSON.stringify(vm), {responseType: 'text'})
      .pipe(
        map(response => response),
        catchError(this.handleError)
      )
  }
  public getCategoryViewModel(): Observable<any> {
    let url = `${this.apiUrl}/category` 
    return this.http.get(url)
      .pipe(
        map(result => {
          return this.mapResult(result);
        }),
        catchError(this.handleError)
      )
  }

  public getCategoryById(categoryId: number): Observable<any> {
    let url = `${this.apiUrl}/category/${categoryId}`; 
    return this.http.get(url)
      .pipe(
        map(result => {
          return this.mapResult(result);
        }),
        catchError(this.handleError)
      )
  }

  public getAll(): Observable<any> {
    let url = `${this.apiUrl}/category/all`
    return this.http.get(url)
      .pipe(
        map(result => result),
        catchError(this.handleError)
      )
  }

  public getAllFiltered(filter: any): Observable<any> {
    let url = `${this.apiUrl}/category/all`
    return this.http.get(url)
      .pipe(
        map(result => result),
        catchError(this.handleError)
      )
  }
  
  public deleteCategory(categoryId: number): Observable<any> {
    let url = `${this.apiUrl}/category/delete/${categoryId}` 
    return this.http.delete(url, {responseType: 'text'})
      .pipe(
        map(result => {
          return result;
        }),
        catchError(this.handleError)
      )
  }

    public mapResult(response: any): Category {
        return new Category(
            response.id,
            response.name,
            response.description
        )
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
      debugger
      console.log(errorMessage);
      return throwError(errorMessage);
    }
}


