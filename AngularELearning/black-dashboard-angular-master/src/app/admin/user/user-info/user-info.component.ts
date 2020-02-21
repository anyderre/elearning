import { Component, OnDestroy, OnInit } from '@angular/core';
import { User } from '../shared/user.model';
import { Subscription } from 'rxjs';
import { UserService } from '../shared/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-user-info',
  templateUrl: 'user-info.component.html',
  styleUrls: ['user-info.component.css']
})
export class UserInfoComponent implements OnInit, OnDestroy {
   public saving = false;
   public subscription: Subscription;
   public userList: User[];
   public vm: User;

   constructor(private userService: UserService, private router: Router) { }

  ngOnInit() {
   this.loadVm();
  }

  public loadVm(): void {
    this.subscription = this.userService.getAll()
    .subscribe(
      data => {
        this.userList = data;
        console.log(data);
      },
      error => {
        this.saving = false;
        alert('Failed to load users');
      });
  }

  public add(): void {
    this.router.navigate(['/admin/user/form']);
  }
  // public getVm(): void {
  //   this.subscription = this.userService.getUserViewModel()
  //   .subscribe(
  //     data => {
  //       this.vm = data;
  //     },
  //     error => {
  //       this.saving = false;
  //       alert('Failed to load user');
  //     });
  // }

  // public getById(userId: number): void {
  //   this.subscription = this.userService.getUserById(userId)
  //   .subscribe(
  //     data => {
  //       this.vm = data;
  //     },
  //     error => {
  //       this.saving = false;
  //       alert('Failed to load user');
  //     });
  // }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  // public save(): void {
  //   if (!this.isValid()) {
  //     return;
  //   }
  //   this.saving = true;
  //   this.userService.saveOrEditUser(this.vm)
  //   .subscribe(message => {
  //     this.saving = false;
  //     alert(message);
  //     },
  //     error => {
  //       this.saving = false;
  //       alert(error);
  //     }
  //   );
  // }

  public edit(index: number): void {
    // this.vm = <User>JSON.parse(JSON.stringify(this.userList[index]));
    this.router.navigate([`/admin/user/${this.userList[index].id}/form`]);
  }

  public back(data: boolean): void {
    if (!data) {
      this.loadVm();
    }
    this.vm = null;
  }

  public isValid(): boolean {
    if (this.vm.name === '') {
      alert('You should specify the user name.');
      return false;
    }
    return true;
  }

  public vmChanged(data: any): void {
    this.loadVm();
  }

  public delete(userId: number): void {
    const user = this.userList.find(o => o.id === userId);
    if (!user) {
      return;
    }

    if (confirm(`Do you really want to delete the user ${user.name}`)) {
      this.subscription = this.userService.deleteUser(userId)
      .subscribe(
        data => {
          alert(data);
          this.loadVm();
        },
        error => {
          console.log(error);
          alert('Failed to delete that user');
        });
    }
  }
}
