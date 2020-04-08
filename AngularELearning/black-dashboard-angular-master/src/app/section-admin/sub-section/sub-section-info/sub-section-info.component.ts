import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { SubSection } from '../shared/sub-section.model';
import { SubSectionService } from '../shared/sub-section.service';

@Component({
  selector: 'app-section-admin-sub-section-info',
  templateUrl: 'sub-section-info.component.html',
})
export class SubSectionInfoComponent implements OnInit, OnDestroy {
  public saving = false;
  public subscription: Subscription;
  public subSectionList: SubSection[];
  public vm: SubSection;
  public subSectionSelected = 0;

  constructor(private subSectionService: SubSectionService, private router: Router) { }

  ngOnInit() {
    this.loadVm();
  }

  public loadVm(): void {
    this.subscription = this.subSectionService.getInfo()
    .subscribe(
      data => {
        this.subSectionList = data;
      },
      () => {
        this.saving = false;
        alert('Failed to load subCategories');
      });
  }

  public add(): void {
    this.router.navigate(['/section-admin/sub-section/form']);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  public edit(index: number): void {
    this.router.navigate([`/section-admin/sub-section/${this.subSectionList[index].id}/form`]);
  }

  public back(data: boolean): void {
    if (!data) {
      this.loadVm();
    }
    this.vm = null;
  }

  public delete(subSectionId: number): void {
    const subSection = this.subSectionList.find(o => o.id === subSectionId);
    if (!subSection) {
      return;
    }

    if (confirm(`Do you really want to delete the subSection ${subSection.name}`)) {
      this.subscription = this.subSectionService.deleteSubSection(subSectionId)
      .subscribe(
        data => {
          alert(data);
          this.loadVm();
        },
        error => {
          console.log(error);
          alert('Failed to delete that subSection');
        });
    }
  }

  public subSectionSelect(): void {
    setTimeout(() => {
      this.subSectionSelected = 0;
      const subSections = this.subSectionList.filter(o => o.selected);
      if (subSections && subSections.length > 0) {
        this.subSectionSelected = subSections.length;
      }
    }, 0);
  }
  public deleteSelected(): void {
    // TODO: endpoint to delete selected subSections
  }
}
