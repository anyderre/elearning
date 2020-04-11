import { Component, OnDestroy, OnInit } from '@angular/core';
import { Rol } from '../shared/role.model';
import { Subscription } from 'rxjs';
import { RoleService } from '../shared/role.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-role-info',
  templateUrl: 'role-info.component.html',
})
export class RoleInfoComponent implements OnInit, OnDestroy {
  public saving = false;
  public subscription: Subscription;
  public roleList: Rol[];
  public vm: Rol;
  public isSuperAdmin = false;

  constructor(private roleService: RoleService, private router: Router) { }

  ngOnInit() {
    this.loadVm();
  }

  public loadVm(): void {
    this.subscription = this.roleService.getInfo()
    .subscribe(
      data => {
        this.roleList = data;
      },
      error => {
        this.saving = false;
        alert('Failed to load roles');
      });
  }

  public add(): void {
    this.router.navigate(['/admin/role/form']);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  public edit(index: number): void {
    this.router.navigate([`/admin/role/${this.roleList[index].id}/form`]);
  }

  public back(data: boolean): void {
    if (!data) {
      this.loadVm();
    }
    this.vm = null;
  }

  public vmChanged(data: any): void {
    this.loadVm();
  }

  public delete(roleId: number): void {
    const role = this.roleList.find(o => o.id === roleId);
    if (!role) {
      return;
    }

    if (confirm(`Do you really want to delete the role ${role.name}`)) {
      this.subscription = this.roleService.deleteRole(roleId)
      .subscribe(
        data => {
          alert(data);
          this.loadVm();
        },
        error => {
          console.log(error);
          alert('Failed to delete that role');
        });
    }
  }
}
