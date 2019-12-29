import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Syllabus } from './syllabus.model';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SyllabusService {
    private apiUrl = environment.baseUrl;

    constructor(private http: HttpClient) {}

    public saveOrEditSyllabus(vm: Syllabus): Observable<any> {
        const url = `${this.apiUrl}/syllabus/save`;
        return this.http.post(url, JSON.stringify(vm), {responseType: 'text'})
          .pipe(
            map(response => response),
            catchError(this.handleError)
          );
    }
  public getSyllabusViewModel(): Observable<any> {
    const url = `${this.apiUrl}/syllabus`;
    return this.http.get(url)
      .pipe(
        map(result => {
          return this.mapResult(result);
        }),
        catchError(this.handleError)
      );
  }

  public getSyllabusById(syllabusId: number): Observable<any> {
    const url = `${this.apiUrl}/syllabus/${syllabusId}`;
    return this.http.get(url)
      .pipe(
        map(result => {
          return this.mapResult(result);
        }),
        catchError(this.handleError)
      );
  }

  public getAll(): Observable<any> {
    const url = `${this.apiUrl}/syllabus/all`;
    return this.http.get(url)
      .pipe(
        map(result => result),
        catchError(this.handleError)
      );
  }

  public deleteSyllabus(syllabusId: number): Observable<any> {
    const url = `${this.apiUrl}/syllabus/delete/${syllabusId}`;
    return this.http.delete(url, {responseType: 'text'})
      .pipe(
        map(result => {
          return result;
        }),
        catchError(this.handleError)
      );
  }

    public mapResult(response: any): Syllabus {
        return new Syllabus(
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


