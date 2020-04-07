import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { Syllabus } from '../../shared/syllabus.model';
import { ModalManager } from 'ngb-modal';
import { Helper } from 'src/app/shared/helper/helper';

@Component({
  selector: 'app-courses-syllabus-form',
  templateUrl: 'syllabus-form.component.html'
})
export class CourseSyllabusFormComponent implements OnInit {
  @Output() goBack = new EventEmitter();
  @Input() details: Syllabus[] = [];
  public formIndex = -1;

  public editing = false;
  public vm: Syllabus;

  ngOnInit() {}

  public add(): void {
    this.vm = new Syllabus(0, '' , []);
    this.formIndex = -1;
    this.editing = true;
  }

  public cancel(): void {
    this.vm = null;
    this.editing = false;
  }

  public edit(index: number): void {
    this.vm = JSON.parse(JSON.stringify(this.details[index]));
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
    if (Helper.getStringValue(this.vm.chapterTitle) === '') {
      alert('You should specify a the chapter title for the syllabus');
      return false;
    }

    if (this.vm.chapterTuts.some(o => o.updating)) {
      alert('There is a video in edition.');
      return;
    }

    if (!this.validateDuplicity()) {
      return false;
    }
    return true;
  }

  private validateDuplicity(): boolean {
    for (let i = 0; i < this.details.length; i++) {
      if (this.formIndex !== i &&
         this.vm.chapterTitle === this.details[i].chapterTitle) {
        alert('This title already exist for another definition.');
        return false;
      }
    }
    return true;
  }
}
