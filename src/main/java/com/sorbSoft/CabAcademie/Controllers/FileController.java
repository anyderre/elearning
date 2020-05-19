package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Services.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private AmazonClient amazonClient;

    @Autowired
    FileController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @PostMapping("/uploadToS3")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) throws Exception {
        return this.amazonClient.uploadFile(file);
    }

    @DeleteMapping("/deleteAtS3")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) throws Exception {
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }

}
