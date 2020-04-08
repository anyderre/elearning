import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { SubSection } from './sub-section.model';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SubSectionService {
    private apiUrl = environment.baseUrl;

    constructor(private http: HttpClient) {}

    public saveOrEditSubSection(vm: SubSection): Observable<any> {
      const url = `${this.apiUrl}/subSection/save`;
      return this.http.post(url, JSON.stringify(vm), {responseType: 'text'})
        .pipe(
          map(response => response),
          catchError(this.handleError)
        );
    }

  public getSubSectionViewModel(): Observable<any> {
    const url = `${this.apiUrl}/subSection`;
    return this.http.get(url)
      .pipe(
        map(result => {
          return this.mapResult(result);
        }),
        catchError(this.handleError)
      );
  }

  public getSubSectionById(subSectionId: number): Observable<any> {
    const url = `${this.apiUrl}/subSection/${subSectionId}`;
    return this.http.get(url)
      .pipe(
        map(result => {
          return this.mapResult(result);
        }),
        catchError(this.handleError)
      );
  }

  public getAll(): Observable<any> {
    const url = `${this.apiUrl}/subSection/all`;
    return this.http.get(url)
      .pipe(
        map(result => result),
        catchError(this.handleError)
      );
  }

  public getInfo(): Observable<any> {
    const url = `${this.apiUrl}/subSection/info`;
    return this.http.get(url)
      .pipe(
        map(result => result),
        catchError(this.handleError)
      );
  }

  public deleteSubSection(subSectionId: number): Observable<any> {
    const url = `${this.apiUrl}/subSection/delete/${subSectionId}`;
    return this.http.delete(url, {responseType: 'text'})
      .pipe(
        map(result => {
          return result;
        }),
        catchError(this.handleError)
      );
  }

    public mapResult(response: any): SubSection {
      return new SubSection(
        response.id,
        response.name,
        response.description,
        response.selected,
        response.section,
        response.allSections,
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


