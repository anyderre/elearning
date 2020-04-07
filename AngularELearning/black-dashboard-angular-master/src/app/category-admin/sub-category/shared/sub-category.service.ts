import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { SubCategory } from './sub-category.model';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SubCategoryService {
    private apiUrl = environment.baseUrl;

    constructor(private http: HttpClient) {}

    public saveOrEditSubCategory(vm: SubCategory): Observable<any> {
      const url = `${this.apiUrl}/subCategory/save`;
      return this.http.post(url, JSON.stringify(vm), {responseType: 'text'})
        .pipe(
          map(response => response),
          catchError(this.handleError)
        );
    }

  public getSubCategoryViewModel(): Observable<any> {
    const url = `${this.apiUrl}/subCategory`;
    return this.http.get(url)
      .pipe(
        map(result => {
          return this.mapResult(result);
        }),
        catchError(this.handleError)
      );
  }

  public getSubCategoryById(subCategoryId: number): Observable<any> {
    const url = `${this.apiUrl}/subCategory/${subCategoryId}`;
    return this.http.get(url)
      .pipe(
        map(result => {
          return this.mapResult(result);
        }),
        catchError(this.handleError)
      );
  }

  public getAll(): Observable<any> {
    const url = `${this.apiUrl}/subCategory/all`;
    return this.http.get(url)
      .pipe(
        map(result => result),
        catchError(this.handleError)
      );
  }

  public getInfo(): Observable<any> {
    const url = `${this.apiUrl}/subCategory/info`;
    return this.http.get(url)
      .pipe(
        map(result => result),
        catchError(this.handleError)
      );
  }

  public deleteSubCategory(subCategoryId: number): Observable<any> {
    const url = `${this.apiUrl}/subCategory/delete/${subCategoryId}`;
    return this.http.delete(url, {responseType: 'text'})
      .pipe(
        map(result => {
          return result;
        }),
        catchError(this.handleError)
      );
  }

    public mapResult(response: any): SubCategory {
      return new SubCategory(
        response.id,
        response.name,
        response.description,
        response.selected,
        response.subCategories,
        response.category,
        response.allCategories,
        response.allSubCategories,
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


