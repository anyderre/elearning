import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Section } from '../shared/section.model';
import { SectionService } from '../shared/section.service';
import { Subscription } from 'rxjs';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-courses-section-form',
  templateUrl: 'section-form.component.html'
})
export class SectionFormComponent {
  @Output() goBack = new EventEmitter();
  @Input() vm: Section;

  public saving = false;
  public sectionList: Section[];

  constructor(private sectionService: SectionService) {}

  public save(form: FormGroup): void {
    if (form.invalid) {
      alert('You should complete all the required fields');
      return;
    }

    if (!this.isValid()) {
      return;
    }
    this.saving = true;
    this.sectionService.saveOrEditSection(this.vm)
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
    this.vm.description = '';
    this.vm.name = '';
  }

  public isValid(): boolean {
    if (this.vm.name == '') {
      alert('You should specify the section name.');
      return false;
    }
    return true;
  }
}
