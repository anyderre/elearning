import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { UserService } from '../shared/User.service';
import { FormGroup } from '@angular/forms';
import { User } from '../shared/user.model';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/internal/Subscription';
import { Location } from '@angular/common';
import { Helper } from 'src/app/shared/helper/helper';

@Component({
  selector: 'app-admin-user-form',
  templateUrl: 'user-form.component.html'
})
export class UserFormComponent implements OnInit {
  @Output() goBack = new EventEmitter();
  @Input() vm: User;
  public subscription: Subscription;
  public saving = false;
  public userList: User[];
  public cleanVm: User;

  constructor(
    private userService: UserService,
    private location: Location,
    private route: ActivatedRoute) {}

  ngOnInit() {
    const id = +this.route.snapshot.paramMap.get('id');
    if (id === 0 ) {
      this.getVm();
    } else {
      this.getById(id);
    }
  }

  public getVm(): void {
    this.subscription = this.userService.getUserViewModel()
    .subscribe(
      data => {
        this.vm = data;
        this.cleanVm = JSON.parse(JSON.stringify(this.vm));
      }, () => {
        this.saving = false;
        alert('Failed to load the user');
      });
  }

  public getById(userId: number): void {
    this.subscription = this.userService.getUserById(userId)
    .subscribe(
      data => {
        this.vm = data;
        this.cleanVm = JSON.parse(JSON.stringify(this.vm));
      },
      () => {
        this.saving = false;
        alert('Failed to load the user');
      });
  }

  public save(form: FormGroup): void {
    if (form.invalid) {
      alert('You should complete all the required fields');
      return;
    }

    if (!this.isValid()) {
      return;
    }
    this.completeVm();
    this.saving = true;
    this.userService.saveOrEditUser(this.vm)
    .subscribe(() => {
      this.saving = false;
      this.back();
      alert('Registration correct');
      },
      () => {
        this.saving = false;
        alert('Registration failed');
      }
    );
  }

  public back(): void {
    this.location.back();
  }

  public cancel(): void {
    if (JSON.stringify(this.cleanVm) !== JSON.stringify(this.vm)) {
      if (confirm('There are changes that will be lost. Do you really want to leave?')) {
        this.location.back();
      }
    } else {
      this.location.back();
    }
  }

  private completeVm(): void {
    this.vm.agreeWithTerms = true;
  }

  private isValid(): boolean {
    if (Helper.getNumericValue(this.vm.role.id) <= 0) {
      alert('You should specify the role.');
      return false;
    }
    if (this.vm.firstName === '') {
      alert('You should specify the first name.');
      return false;
    }
    if (this.vm.lastName === '') {
      alert('You should specify the first name.');
      return false;
    }
    if (this.vm.email === '') {
      alert('You should specify the email.');
      return false;
    }
    if (this.vm.username === '') {
      alert('You should specify the username.');
      return false;
    }
    if (Helper.getNumericValue(this.vm.role.id) <= 0) {
      alert('You should specify the role.');
      return false;
    }
    if (this.vm.id <= 0) {
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
    }
    return true;
  }
}
