import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpResponse } from '@angular/common/http';
import { Observable, from } from 'rxjs';

export class AngularDateSentHttpInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return from(this.handleAccess(req, next));
  }

  private async handleAccess(request: HttpRequest<any>, next: HttpHandler): Promise<HttpEvent<any>> {
    // Only add to known domains since we don't want to send our tokens to just anyone.
    // Also, Giphy's API fails when the request includes a token.

    if ((request.method === 'POST' || request.method === 'post') || (request.method === 'PUT' || request.method === 'put')) {
      debugger
      request = request.clone({
        body: this.convert(request.body)
      });
      // this.convert(request.body);
    }
    // if (request.urlWithParams.indexOf('localhost') > -1) {
    //   if (request.urlWithParams.indexOf('/authenticate') > -1) {
    //     request = request.clone({
    //       setHeaders: {
    //         'Content-Type': 'application/json'
    //       }
    //     });
    //   } else {
    //     const accessToken =  localStorage.getItem('access_token');
    //     request = request.clone({
    //       setHeaders: {
    //         Authorization: 'Bearer ' + accessToken,
    //         'Content-Type': 'application/json'
    //       }
    //     });
    //   }
    // }
    return next.handle(request).toPromise();
  }

  private isDate(date: string): boolean {
    return !isNaN(Date.parse(date));
  }

  private convert(body: any) {
    if (body === null || body === undefined ) {
      return body;
    }
    if (typeof body !== 'object') {
      return body;
    }
    for (const key of Object.keys(body)) {
      const value = body[key];
      if (this.isDate(value)) {

        body[key] = new Date(value);
        body[key] = body[key].toISOString();
      } else if (typeof value === 'object') {
        this.convert(value);
      }
    }
  }
}
