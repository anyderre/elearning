import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { Syllabus } from '../shared/syllabus.model';

@Component({
  selector: 'app-courses-syllabus-form',
  templateUrl: 'syllabus-form.component.html'
})
export class SyllabusFormComponent implements OnInit {
  @Output() goBack = new EventEmitter();
  @Input() details: Syllabus[] = [];
  public formIndex = -1;

  public editing = false;
  public vm: Syllabus;

  ngOnInit() {}

  public add(): void {
    this.vm = new Syllabus(0, '' , '');
    this.formIndex = -1;
    this.editing = true;
  }

  public cancel(): void {
    this.vm = null;
    this.editing = false;
  }

  public edit(index: number): void {
    this.vm = this.details[index];
    this.formIndex = index;
    this.editing = true;
  }

  public delete(index: number, title: string): void {
    if (confirm('Are you sure to delete the syllabus: ' + title + '?')) {
      this.details.splice(index);
    }
  }

  public accept(): void {
    if (this.isValid()) {
      if (+this.formIndex <= 0) {
        this.details.push(this.vm);
      } else {
        let item = this.details[this.formIndex];
        if (item) {
          item = this.vm;
        }
      }
      this.editing = false;
    }
  }

  public isValid(): boolean {
    if (this.vm.title === '') {
      alert('You should specify a title for the syllabus');
      return false;
    }
    if (this.vm.description === '') {
      alert('You should specify a description for the syllabus');
      return false;
    }
    if (!this.validateDuplicity()) {
      return false;
    }
    return true;
  }

  private validateDuplicity(): boolean {
    for (let i = 0; i < this.details.length; i++) {
      if (this.formIndex !== i && this.vm.title === this.details[i].title) {
        alert('This title already exist for another definition.');
        return false;
      }
    }
    return true;
  }
}
