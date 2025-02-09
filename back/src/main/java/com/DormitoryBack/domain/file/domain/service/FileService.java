package com.DormitoryBack.domain.file.domain.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
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

    public String generatePresignedURL(String fileName){
        Date expiration=new Date();
        long expTimeMillis=expiration.getTime();
        expTimeMillis+=1000*60*60; //1시간
        expiration.setTime(expTimeMillis);
        
        GeneratePresignedUrlRequest generatePresignedUrlRequest=
            new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        
        URL url=s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        
        return url.toString();
    }

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
