import { Component, Input, Output, EventEmitter } from '@angular/core';
import { UserService } from '../shared/User.service';
import { FormGroup } from '@angular/forms';
import { User } from '../shared/user.model';

@Component({
  selector: 'app-admin-user-form',
  templateUrl: 'user-form.component.html'
})
export class UserFormComponent {
  @Output() goBack = new EventEmitter();
  @Input() vm: User;

  public saving = false;
  public userList: User[];

  constructor(private userService: UserService) {}

  public save(form: FormGroup): void {
    if (form.invalid) {
      alert('You should complete all the required fields');
      return;
    }

    if (!this.isValid()) {
      return;
    }
    this.saving = true;
    this.userService.saveOrEditUser(this.vm)
    .subscribe(() => {
      this.saving = false;
      this.back(false);
      alert('Registration correct');
      },
      () => {
        this.saving = false;
        alert('Registration failed');
      }
    );
  }

  public back(isBack: boolean): void {
    if (this.goBack.observers.length > 0) {
      this.goBack.emit(isBack);
    }
  }

  public cancel(): void {
    this.vm.name = '';
    this.vm.username = '';
    this.vm.password = '';
    this.vm.passwordConfirm = '';
    this.vm.isProfessor = false;
  }

  public isValid(): boolean {
    if (this.vm.name === '') {
      alert('You should specify the name.');
      return false;
    }
    if (this.vm.username === '') {
      alert('You should specify the username.');
      return false;
    }
    if (this.vm.password === '') {
      alert('You should specify the password.');
      return false;
    }
    if (this.vm.passwordConfirm === '') {
      alert('You should specify the password confirmation.');
      return false;
    }
    if (this.vm.password !== this.vm.passwordConfirm) {
      alert('The password should be the same.');
      return false;
    }
    return true;
  }
}
