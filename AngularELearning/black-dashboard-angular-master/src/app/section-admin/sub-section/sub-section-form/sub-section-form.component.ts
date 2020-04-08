import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/internal/Subscription';
import { Location } from '@angular/common';
import { Helper } from 'src/app/shared/helper/helper';
import { SubSection } from '../shared/sub-section.model';
import { SubSectionService } from '../shared/sub-section.service';

@Component({
  selector: 'app-section-admin-sub-section-form',
  templateUrl: 'sub-section-form.component.html'
})
export class SubSectionFormComponent implements OnInit {
  @Output() goBack = new EventEmitter();
  @Input() vm: SubSection;
  public subscription: Subscription;
  public saving = false;
  public subSectionList: SubSection[];
  public cleanVm: SubSection;

  constructor(
    private subSectionService: SubSectionService,
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
    this.subscription = this.subSectionService.getSubSectionViewModel()
    .subscribe(
      data => {
        this.vm = data;
        this.cleanVm = JSON.parse(JSON.stringify(this.vm));
      }, () => {
        this.saving = false;
        alert('Failed to load the subSection');
      });
  }

  public getById(subSectionId: number): void {
    this.subscription = this.subSectionService.getSubSectionById(subSectionId)
    .subscribe(
      data => {
        this.vm = data;
        this.cleanVm = JSON.parse(JSON.stringify(this.vm));
      },
      () => {
        this.saving = false;
        alert('Failed to load the subSection');
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
    this.subSectionService.saveOrEditSubSection(this.vm)
    .subscribe(() => {
      this.saving = false;
      alert('Registration correct');
      this.router.navigate(['/section-admin/sub-section/info']);
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

  private isValid(): boolean {
    if (this.vm.name === '') {
      alert('You should specify the name.');
      return false;
    }
    if (this.vm.section && Helper.getNumericValue(this.vm.section.id) <= 0) {
      alert('You should specify the section.');
      return false;
    }
    return true;
  }
}
