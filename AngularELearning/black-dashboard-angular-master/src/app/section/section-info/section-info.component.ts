import { Component, Input } from '@angular/core';
import { Section } from '../shared/section.model';
import { Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { SectionService } from '../shared/section.service';

@Component({
  selector: 'app-courses-section-info',
  templateUrl: 'section-info.component.html',
  styleUrls: ['section-info.component.css']
})
export class SectionInfoComponent {
   public saving = false;
   public subscription: Subscription;
   public sectionList: Section[];
   public vm: Section;

   constructor(private route: ActivatedRoute,
     private router: Router,
     private sectionService: SectionService) {
    }
  ngOnInit(){
   this.loadVm();
  }

  public loadVm():void {
    this.subscription = this.sectionService.getAll()
    .subscribe(
      data => {
        this.sectionList = data;
        console.log(data);
      },
      error => {
        this.saving = false;  
        alert('Failed to load sections');
      });
  }

  public getVm(): void {
    this.subscription = this.sectionService.getSectionViewModel()
    .subscribe(
      data => {
        this.vm = data;
      },
      error => {
        this.saving = false;  
        alert('Failed to load section');
      });
  }

  public getById(sectionId: number): void {
    this.subscription = this.sectionService.getSectionById(sectionId)
    .subscribe(
      data => {
        this.vm = data;
      },
      error => {
        this.saving = false;  
        alert('Failed to load section');
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
    this.sectionService.saveOrEditSection(this.vm)
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
    // this.vm = <Section>JSON.parse(JSON.stringify(this.sectionList[index]));
    this.getById(this.sectionList[index].id);
  }

  public back(data: boolean): void {
    if (!data) {
      this.loadVm();
    } 
    this.vm = null
  }

  public isValid(): boolean {
    if (this.vm.name == '') {
      alert('You should specify the section name.');
      return false;
    }
    return true;
  }

  public vmChanged(data: any): void {
    this.loadVm(); 
  }

  public delete (sectionId: number): void {
    const section = this.sectionList.find(o => o.id === sectionId);
    if (!section){
      return;
    }

    if (confirm(`Do you really want to delete the section ${section.name}`)) {
      this.subscription = this.sectionService.deleteSection(sectionId)
      .subscribe(
        data => {
          alert(data);
          this.loadVm();
        },
        error => {
          console.log(error);
          alert('Failed to delete that section');
        });
    }
  }
  
}