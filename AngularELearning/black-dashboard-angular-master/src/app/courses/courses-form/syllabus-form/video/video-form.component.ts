import { Component, Input, Output, EventEmitter, OnInit, ViewChild, ElementRef, ContentChild, ViewEncapsulation } from '@angular/core';
import { Video } from 'src/app/courses/shared/video.model';
import { VideoUploadService } from '../shared/video-upload.service';
import { Helper } from 'src/app/shared/helper/helper';

@Component({
  selector: 'app-courses-syllabus-video-form',
  templateUrl: 'video-form.component.html',
  encapsulation: ViewEncapsulation.None
})
export class CourseSyllabusVideoFormComponent implements OnInit {
  @Output() goBack = new EventEmitter();
  @Input() details: Video[] = [];
  public formIndex = -1;
  @ViewChild('content', {static: false}) myModal;

  public selectedFiles: FileList;

  public vm: Video;

  ngOnInit() {}

  constructor(private videoService: VideoUploadService) { }

  public upload(): void {
    const file = this.selectedFiles.item(0);
    /* There we call aws3 to save video*/
    // this.uploadService.uploadfile(file);
  }

  public selectFile(event: any, index: number): void {
    this.details[index].selectedFile = (event.target as HTMLInputElement).files[0];
    this.details[index].videoURL = `https://YOUR S3 BUCKET NAME.s3.amazonaws.com/${this.details[index].selectedFile.name}`;
  }

  public add(): void {
    if (this.existInEdition()) {
      alert('There is another video in edition.');
      return;
    }
    this.vm = new Video(0, '', '' , true, null);
    this.details.push(JSON.parse(JSON.stringify(this.vm)));
  }

  public cancelEdit(index: number): void {
    if (this.vm.id === 0 && Helper.getStringValue(this.vm.videoTitle) === '') {
      this.details.splice(index);
    } else {
      this.details[index] = JSON.parse(JSON.stringify(this.vm));
      this.details[index].updating = false;
    }
    this.vm = null;
  }
  public acceptEdit(index: number): void {
    if (!this.isValid(index)) {
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
      alert('There is another video in edition.');
      return;
    }
    this.vm = JSON.parse(JSON.stringify(this.details[index]));
    this.details[index].updating = true;
  }

  public delete(index: number, title: string): void {
    if (confirm('Are you sure to delete this video?')) {
      this.details.splice(index);
    }
  }

  private validateDuplicity(index: number): boolean {
    for (let i = 0; i < this.details.length; i++) {
      if (index !== i && this.vm.videoTitle === this.details[i].videoTitle) {
        alert('This description already exist for another definition.');
        return false;
      }
    }
    return true;
  }

  private isValid(index: number): boolean {
    if (Helper.getStringValue(this.details[index].videoTitle) === '') {
      alert('You should specify the title');
      return false;
    }
    if (!this.details[index].selectedFile) {
      alert('You should specify the video');
      return false;
    }
    return true;
  }
}
