import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Syllabus } from '../shared/syllabus.model';
import { SyllabusService } from '../shared/syllabus.service';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-courses-syllabus-form',
  templateUrl: 'syllabus-form.component.html'
})
export class SyllabusFormComponent {
  @Output() goBack = new EventEmitter();
  @Input() vm: Syllabus;

  public saving = false;
  public syllabusList: Syllabus[];

  constructor(private syllabusService: SyllabusService) {}

  public save(form: FormGroup): void {
    if (form.invalid) {
      alert('You should complete all the required fields');
      return;
    }

    if (!this.isValid()) {
      return;
    }
    this.saving = true;
    this.syllabusService.saveOrEditSyllabus(this.vm)
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
    if (this.vm.name === '') {
      alert('You should specify the syllabus name.');
      return false;
    }
    return true;
  }
}
