import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { RoleService } from '../shared/Role.service';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/internal/Subscription';
import { Location } from '@angular/common';
import { Rol } from '../shared/role.model';

@Component({
  selector: 'app-admin-role-form',
  templateUrl: 'role-form.component.html'
})
export class RoleFormComponent implements OnInit {
  public subscription: Subscription;
  public saving = false;
  public roleList: Rol[];
  public cleanVm: Rol;
  public vm: Rol;

  constructor(
    private roleService: RoleService,
    private location: Location,
    private router: Router,
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
    this.subscription = this.roleService.getRoleViewModel()
    .subscribe(
      data => {
        this.vm = data;
        this.cleanVm = JSON.parse(JSON.stringify(this.vm));
      }, () => {
        this.saving = false;
        alert('Failed to load the role');
      });
  }

  public getById(roleId: number): void {
    this.subscription = this.roleService.getRoleById(roleId)
    .subscribe(
      data => {
        this.vm = data;
        this.cleanVm = JSON.parse(JSON.stringify(this.vm));
      },
      () => {
        this.saving = false;
        alert('Failed to load the role');
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
    this.saving = true;
    this.roleService.saveOrEditRole(this.vm)
    .subscribe(() => {
      this.saving = false;
      this.router.navigate(['/admin/role/info']);
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

  public isValid(): boolean {
    if (this.vm.name === '') {
      alert('You should specify the name.');
      return false;
    }
    return true;
  }
}
