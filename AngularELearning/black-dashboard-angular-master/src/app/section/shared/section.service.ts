import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Section } from './section.model';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SectionService {
    private apiUrl = environment.baseUrl;

    constructor(private http: HttpClient) {}

    public saveOrEditSection(vm: Section): Observable<any> {
        const url = `${this.apiUrl}/section/save`;
        return this.http.post(url, JSON.stringify(vm), {responseType: 'text'})
          .pipe(
            map(response => response),
            catchError(this.handleError)
          );
    }
  public getSectionViewModel(): Observable<any> {
    const url = `${this.apiUrl}/section`;
    return this.http.get(url)
      .pipe(
        map(result => {
          return this.mapResult(result);
        }),
        catchError(this.handleError)
      );
  }

  public getSectionById(sectionId: number): Observable<any> {
    const url = `${this.apiUrl}/section/${sectionId}`;
    return this.http.get(url)
      .pipe(
        map(result => {
          return this.mapResult(result);
        }),
        catchError(this.handleError)
      );
  }

  public getAll(): Observable<any> {
    const url = `${this.apiUrl}/section/all`;
    return this.http.get(url)
      .pipe(
        map(result => result),
        catchError(this.handleError)
      );
  }

  public deleteSection(sectionId: number): Observable<any> {
    const url = `${this.apiUrl}/section/delete/${sectionId}`;
    return this.http.delete(url, {responseType: 'text'})
      .pipe(
        map(result => {
          return result;
        }),
        catchError(this.handleError)
      );
  }

    public mapResult(response: any): Section {
        return new Section(
            response.id,
            response.name,
            response.description
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


