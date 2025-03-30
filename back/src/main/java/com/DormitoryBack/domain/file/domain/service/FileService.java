package com.DormitoryBack.domain.file.domain.service;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.DormitoryBack.domain.file.domain.enums.ParamType;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.service.UserServiceExternal;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public void setBucketName(String bucketName){ //테스트 용도
        this.bucketName=bucketName;
    }

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserServiceExternal userService;

    public String generatePresignedURL(ParamType paramType, String userInfo){
        Long userId;
        if(paramType==ParamType.NICKNAME){
            String nickname=userInfo;
            userId=userService.getUserIdFromNickname(nickname);
        }else if(paramType==ParamType.USERID){
            userId=Long.valueOf(userInfo);
        }else{
            throw new RuntimeException("InvalidType : type must be NICKNAME or USERID"); //custom exception 필요..
        }

        String fileName=userService.getUserImageName(userId);
        if(fileName==null){
            throw new RuntimeException("NoUserImage");
        }

        Date expiration=new Date();
        long expTimeMillis=expiration.getTime();
        expTimeMillis+=1000*60*60; //1시간
        expiration.setTime(expTimeMillis);
        
        GeneratePresignedUrlRequest generatePresignedUrlRequest=
            new GeneratePresignedUrlRequest(bucketName, fileName)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        
        URL url=s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        String urlString=url.toString();
        return urlString;
    }

    public void setUserProfileImage(MultipartFile file, String token){
        if(!tokenProvider.validateToken(token)){
            throw new RuntimeException("InvalidToken");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        String userImageName=userService.getUserImageName(userId);
        log.info(userImageName);
        if(userImageName!=null){
            deleteUserProfileImage(token);
        }
        try{
            userImageName=uploadFile(file);
            userService.saveUserProfileImage(userId, userImageName);
        }catch(IOException e){
            throw new RuntimeException(e.getMessage());
        }    
    }

    public void deleteUserProfileImage(String token){
        if(!tokenProvider.validateToken(token)){
            throw new RuntimeException("InvalidToken");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        String imageName=userService.deleteUserProfileImage(userId);
        log.info(imageName);
        deleteFile(imageName);
    }

    public String uploadFile(MultipartFile file) throws IOException{
        String fileName=generateFileName(file); //fileName은 UUID와 업로드할 때 파일의 이름이 조합됨. 따라서 절대 중복될 수 없음.
        try{
            s3Client.putObject(bucketName, fileName, file.getInputStream(), getObjectMetadata(file));
            return fileName; 
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

    public void deleteFile(String fileName){
        try{
            s3Client.deleteObject(bucketName,fileName);
        }catch(AmazonServiceException e){
            throw new RuntimeException("AmazonServiceException : "+e.getMessage());
        }
    }


 
}
