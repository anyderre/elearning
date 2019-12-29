import { Component, OnDestroy, OnInit } from '@angular/core';
import { Syllabus } from '../shared/syllabus.model';
import { Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { SyllabusService } from '../shared/syllabus.service';

@Component({
  selector: 'app-courses-syllabus-info',
  templateUrl: 'syllabus-info.component.html',
  styleUrls: ['syllabus-info.component.css']
})
export class SyllabusInfoComponent implements OnInit, OnDestroy {
   public saving = false;
   public subscription: Subscription;
   public syllabusList: Syllabus[];
   public vm: Syllabus;

   constructor(
     private route: ActivatedRoute,
     private router: Router,
     private syllabusService: SyllabusService) {
    }
  ngOnInit() {
   this.loadVm();
  }

  public loadVm(): void {
    this.subscription = this.syllabusService.getAll()
    .subscribe(
      data => {
        this.syllabusList = data;
        console.log(data);
      },
      error => {
        this.saving = false;
        alert('Failed to load syllabuss');
      });
  }

  public getVm(): void {
    this.subscription = this.syllabusService.getSyllabusViewModel()
    .subscribe(
      data => {
        this.vm = data;
      },
      error => {
        this.saving = false;
        alert('Failed to load syllabus');
      });
  }

  public getById(syllabusId: number): void {
    this.subscription = this.syllabusService.getSyllabusById(syllabusId)
    .subscribe(
      data => {
        this.vm = data;
      },
      error => {
        this.saving = false;
        alert('Failed to load syllabus');
      });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  public save(): void {
    if (!this.isValid()) {
      return;
    }
    this.saving = true;
    this.syllabusService.saveOrEditSyllabus(this.vm)
    .subscribe(message => {
      this.saving = false;
      alert(message);
      },
      error => {
        this.saving = false;
        alert(error);
      }
    );
  }

  public edit(index: number): void {
    // this.vm = <Syllabus>JSON.parse(JSON.stringify(this.syllabusList[index]));
    this.getById(this.syllabusList[index].id);
  }

  public back(data: boolean): void {
    if (!data) {
      this.loadVm();
    }
    this.vm = null;
  }

  public isValid(): boolean {
    if (this.vm.name === '') {
      alert('You should specify the syllabus name.');
      return false;
    }
    return true;
  }

  public vmChanged(data: any): void {
    this.loadVm();
  }

  public delete(syllabusId: number): void {
    const syllabus = this.syllabusList.find(o => o.id === syllabusId);
    if (!syllabus) {
      return;
    }

    if (confirm(`Do you really want to delete the syllabus ${syllabus.name}`)) {
      this.subscription = this.syllabusService.deleteSyllabus(syllabusId)
      .subscribe(
        data => {
          alert(data);
          this.loadVm();
        },
        error => {
          console.log(error);
          alert('Failed to delete that syllabus');
        });
    }
  }
}
