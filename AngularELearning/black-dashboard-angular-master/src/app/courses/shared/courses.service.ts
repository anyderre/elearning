import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { Courses } from './courses.model';

@Injectable({
  providedIn: 'root'
})
export class CoursesService {
    private apiUrl = environment.baseUrl;

    constructor(private http: HttpClient) {}

  public saveOrEditCourses(vm: Courses): Observable<any> {
    const url = `${this.apiUrl}/courses/save`;
    return this.http.post(url, JSON.stringify(vm), {responseType: 'text'})
      .pipe(
        map(response => response),
        catchError(this.handleError)
      );
  }
  public getCoursesViewModel(): Observable<any> {
    const url = `${this.apiUrl}/course`;
    return this.http.get(url)
      .pipe(
        map(result => {
          return this.mapResult(result);
        }),
        catchError(this.handleError)
      );
  }

  public getCoursesById(coursesId: number): Observable<any> {
    const url = `${this.apiUrl}/courses/${coursesId}`;
    return this.http.get(url)
      .pipe(
        map(result => {
          return this.mapResult(result);
        }),
        catchError(this.handleError)
      );
  }

  public getAll(): Observable<any> {
    const url = `${this.apiUrl}/courses/all`;
    return this.http.get(url)
      .pipe(
        map(result => result),
        catchError(this.handleError)
      );
  }

  public deleteCourses(coursesId: number): Observable<any> {
    const url = `${this.apiUrl}/courses/delete/${coursesId}`;
    return this.http.delete(url, {responseType: 'text'})
      .pipe(
        map(result => {
          return result;
        }),
        catchError(this.handleError)
      );
  }

    public mapResult(response: any): Courses {
      return new Courses(
          response.id,
          response.title,
          response.price,
          response.description,
          response.isPremium,
          response.startDate,
          response.endDate,
          response.syllabus,
          response.category,
          response.section,
          response.user,
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


