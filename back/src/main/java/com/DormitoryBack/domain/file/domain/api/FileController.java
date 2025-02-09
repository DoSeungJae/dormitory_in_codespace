package com.DormitoryBack.domain.file.domain.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.DormitoryBack.domain.file.domain.service.FileService;

@RequestMapping("api/v1/file")
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException{
        String url=fileService.uploadFile(file);
        return ResponseEntity.status(HttpStatus.OK).body(url);        
    }

    @GetMapping("/getURL/{fileName}")
    public ResponseEntity<String> getFile(@PathVariable("fileName") String fileName) throws IOException{
        String presignedURL=fileService.generatePresignedURL(fileName);
        return ResponseEntity.status(HttpStatus.OK).body(presignedURL);
    }

}
