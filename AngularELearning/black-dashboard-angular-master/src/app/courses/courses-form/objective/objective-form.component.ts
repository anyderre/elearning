import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { Objective } from '../../shared/objective.model';
import { Helper } from '../../../shared/helper/helper';

@Component({
  selector: 'app-courses-objective-form',
  templateUrl: 'objective-form.component.html'
})
export class CourseObjectiveFormComponent implements OnInit {
  @Output() goBack = new EventEmitter();
  @Input() details: Objective[] = [];
  public formIndex = -1;

  public vm: Objective;

  ngOnInit() {}

  public add(): void {
    if (this.existInEdition()) {
      alert('There is another objective in edition.');
      return;
    }
    this.vm = new Objective(0, '' , true);
    this.details.push(JSON.parse(JSON.stringify(this.vm)));
  }

  public cancelEdit(index: number): void {
    if (this.vm.id === 0 && Helper.getStringValue(this.vm.description) === '') {
      this.details.splice(index);
    } else {
      this.details[index] = JSON.parse(JSON.stringify(this.vm));
      this.details[index].updating = false;
    }
    this.vm = null;
  }
  public acceptEdit(index: number): void {
    if (Helper.getStringValue(this.details[index].description) === '') {
      alert('You should specify the objective');
      return;
    }
    if (!this.validateDuplicity(index)) {
      return;
    }
    this.details[index].updating = false;
    this.vm = null;
  }

  private existInEdition(): boolean {
    return this.details.some(o => o.updating);
  }

  public edit(index: number): void {
    if (this.existInEdition()) {
      alert('There is another objective in edition.');
      return;
    }
    this.vm = JSON.parse(JSON.stringify(this.details[index]));
    this.details[index].updating = true;
  }

  public delete(index: number, title: string): void {
    if (confirm('Are you sure to delete this objective?')) {
      this.details.splice(index);
    }
  }

  private validateDuplicity(index: number): boolean {
    for (let i = 0; i < this.details.length; i++) {
      if (index !== i && this.vm.description === this.details[i].description) {
        alert('This description already exist for another definition.');
        return false;
      }
    }
    return true;
  }
}
