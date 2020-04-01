import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { Feature } from '../../shared/feature.model';

@Component({
  selector: 'app-courses-overview-feature-form',
  templateUrl: 'feature-form.component.html'
})
export class CourseOverviewFeatureFormComponent implements OnInit {
  @Output() goBack = new EventEmitter();
  @Input() details: Feature[] = [];
  public formIndex = -1;

  public editing = false;
  public vm: Feature;

  ngOnInit() {}

  public add(): void {
    this.vm = new Feature(0, '' , '');
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
    if (confirm('Are you sure to delete the feature: ' + title + '?')) {
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
      alert('You should specify a title for the feature');
      return false;
    }
    if (this.vm.icon === '') {
      alert('You should specify a icon for the feature');
      return false;
    }

    if (!this.validateDuplicity()) {
      return false;
    }
    return true;
  }

  private validateDuplicity(): boolean {
    for (let i = 0; i < this.details.length; i++) {
      if (this.formIndex !== i &&
         this.vm.icon === this.details[i].icon &&
         this.vm.title === this.details[i].title) {
        alert('This title already exist for another definition.');
        return false;
      }
    }
    return true;
  }
}
