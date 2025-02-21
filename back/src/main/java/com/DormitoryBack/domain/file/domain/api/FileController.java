package com.DormitoryBack.domain.file.domain.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.DormitoryBack.domain.file.domain.service.FileService;

@CrossOrigin(origins = {"https://improved-space-tribble-vjvwrwx956jh69w4-3000.app.github.dev", "https://turbo-chainsaw-rpvvwx9pp5c5p55-3000.app.github.dev"})
@RequestMapping("api/v1/file")
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    /* 기본적인 파일 업로드 메서드, 삭제 하는게 맞지만 샘플 용도로 놔둠.
    @PostMapping("")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException{
        String url=fileService.uploadFile(file);
        return ResponseEntity.status(HttpStatus.OK).body(url);        
    }
    */

    /*
    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable("fileName") String fileName){
        fileService.deleteFile(fileName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
    */

    @GetMapping("/userImageUrl")
    public ResponseEntity<String> getFileURL(@RequestHeader("Authorization") String token) throws IOException{
        String presignedURL=fileService.generatePresignedURL(token);
        return ResponseEntity.status(HttpStatus.OK).body(presignedURL);
    }

    @PutMapping("/userImage")
    public ResponseEntity<Void> setUserImage(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token){
        fileService.setUserProfileImage(file, token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }       

    @DeleteMapping("/userImage")
    public ResponseEntity<Void> deleteUserImage(@RequestHeader("Authorization") String token){
        fileService.deleteUserProfileImage(token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
