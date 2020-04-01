import { Injectable } from '@angular/core';
import * as AWS from 'aws-sdk/global';
import * as S3 from 'aws-sdk/clients/s3';

@Injectable({
  providedIn: 'root'
})
export class VideoUploadService {
  FOLDER = 'elearning-videos/';

  constructor() { }

  public uploadfile(file: File): void {

    const bucket = new S3(
      {
        accessKeyId: 'YOUR-ACCESS-KEY-ID',
        secretAccessKey: 'YOUR-SECRET-ACCESS-KEY',
        region: 'YOUR AWS REGION'
      }
    );

    const params = {
      Bucket: 'elearning-video-bucket',
      Key: this.FOLDER + file.name,
      Body: file
    };

    bucket.upload(params, (err: any, data: any) => {
      if (err) {
        console.log('There was an error uploading your file: ', err);
        return false;
      }

      console.log('Successfully uploaded file.', data);
      return true;
    });
  }
  public deletefile(file: File): void {

    const bucket = new S3(
      {
        accessKeyId: 'YOUR-ACCESS-KEY-ID',
        secretAccessKey: 'YOUR-SECRET-ACCESS-KEY',
        region: 'YOUR AWS REGION'
      }
    );

    const params = {
      Bucket: 'elearning-video-bucket',
      Key: this.FOLDER + file.name,
      Body: file
    };

    bucket.deleteObject(params, (err: any, data: any) => {
      if (err) {
        console.log('There was an error deleting your file: ', err);
        return false;
      }

      console.log('Successfully deleted file.', data);
      return true;
    });
  }
}
