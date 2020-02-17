import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable, from } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor() {
  }

  public intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return from(this.handleAccess(request, next));
  }

  private async handleAccess(request: HttpRequest<any>, next: HttpHandler): Promise<HttpEvent<any>> {
    // Only add to known domains since we don't want to send our tokens to just anyone.
    // Also, Giphy's API fails when the request includes a token.
    if (request.urlWithParams.indexOf('localhost') > -1) {
      if (request.urlWithParams.indexOf('/authenticate') > -1) {
        request = request.clone({
          setHeaders: {
            'Content-Type': 'application/json'
          }
        });
      } else {
        const accessToken =  localStorage.getItem('access_token');
        request = request.clone({
          setHeaders: {
            Authorization: 'Bearer ' + accessToken,
            'Content-Type': 'application/json'
          }
        });
      }
    }
    return next.handle(request).toPromise();
  }
}
