import { Component, OnInit } from "@angular/core";
import { User } from './shared/user.model';
import { AuthService } from '../shared/auth.service';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { first } from 'rxjs/operators';

@Component({
  selector: "app-login",
  templateUrl: "login.component.html"
})
export class LoginComponent implements OnInit {
  public vm: User;
  public error: string;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(){
    this.vm = new User('', '');
  }

  public authenticate(form: FormGroup):void {
    if (form.invalid) {
      alert('Credentials incorrect');
      return;
    }
    this.authService.login(this.vm.username, this.vm.password)
    .pipe(first())
    .subscribe(
      result => this.router.navigate(['/dashboard']),
      err => this.error = 'Could not authenticate'
    );
  } 
}
