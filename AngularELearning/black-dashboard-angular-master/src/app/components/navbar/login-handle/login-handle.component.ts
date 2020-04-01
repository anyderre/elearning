import { Component, OnInit } from '@angular/core';
import { OktaAuthService } from '@okta/okta-angular';
import { AuthService } from '../../../shared/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-handle',
  templateUrl: 'login-handle.component.html'
})
export class LoginHandleComponent implements OnInit {
  isAuthenticated: boolean;

  constructor(public authService: AuthService, public router: Router) {
  }

  ngOnInit() {
    this.isAuthenticated = !this.authService.isTokenExpired(localStorage.getItem('acces_token'));
  }

  public login(): void {
    this.router.navigate(['/login']);
  }

  public logout(): void {
    this.authService.logout();
  }
}
