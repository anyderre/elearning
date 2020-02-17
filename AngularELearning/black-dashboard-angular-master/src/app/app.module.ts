import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { ToastrModule } from 'ngx-toastr';

import { AppComponent } from './app.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppRoutingModule } from './app-routing.module';
import { ComponentsModule } from './components/components.module';
import { AuthInterceptor } from './shared/auth/auth.interceptor';
import { LoginComponent } from './login/login.component';
import { BrowserModule } from '@angular/platform-browser';
import { AngularDateHttpInterceptor } from './shared/global/date-http-interceptor';
import { AngularDateSentHttpInterceptor } from './shared/global/date-sent-http-interceptor';

export function tokenGetter() {
  return localStorage.getItem('access_token');
}

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    ComponentsModule,
    NgbModule,
    RouterModule,
    AppRoutingModule,
    ToastrModule.forRoot(),
  ],
  declarations: [
    AppComponent,
    AdminLayoutComponent,
    LoginComponent,
    AuthLayoutComponent],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: AngularDateHttpInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: AngularDateSentHttpInterceptor, multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
