package com.DormitoryBack.domain.file.domain.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public void setBucketName(String bucketName){ //테스트 용도
        this.bucketName=bucketName;
    }

    @Autowired
    private AmazonS3 s3Client;


    private String defaultUrl="https://s3.amazonaws.com";

    public String uploadFile(MultipartFile file) throws IOException{
        String fileName=generateFileName(file);
        try{
            s3Client.putObject(bucketName, fileName, file.getInputStream(), getObjectMetadata(file));
            
            return defaultUrl+fileName;
        }catch(SdkClientException e){
            throw new IOException("S3Error:"+e.getMessage());
        }
    }

    public ObjectMetadata getObjectMetadata(MultipartFile file){
        ObjectMetadata objectMetadata=new ObjectMetadata();

        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        return objectMetadata;
    }

    public String generateFileName(MultipartFile file){
        UUID randomUUID=UUID.randomUUID();
        String stringUUID=randomUUID.toString();
        String fileName=stringUUID+"-"+file.getOriginalFilename();

        return fileName;
    }
 
}
