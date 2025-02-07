package com.DormitoryBack.domain.file.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@ExtendWith(MockitoExtension.class) 이 어노테이션 설정 시 테스트 안됨
public class FileServiceTest {

    @InjectMocks
    private FileService fileService;
    
    @Mock
    private AmazonS3 s3Client;

    @Mock
    private MultipartFile file;

    @Value("cloud.aws.s3.bucket")
    private String bucketName;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        fileService.setBucketName(bucketName);
        
    }

    @Test
    public void testTest(){
        assertEquals("delivery-box", bucketName);
    }

    @Test
    public void testUploadFile() throws IOException {
        String originalFileName="test.txt";
        byte[] content="test content".getBytes();
        when(file.getOriginalFilename()).thenReturn(originalFileName);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(content));
        when(file.getContentType()).thenReturn("text/plain");
        when(file.getSize()).thenReturn((long) content.length);

        //무슨 의미인지 잘 모르겠다.
        doAnswer(invocation -> null).when(s3Client).putObject(anyString(), anyString(), any(), any(ObjectMetadata.class));
        String result=fileService.uploadFile(file);

        assertNotNull(result);
        assertTrue(result.startsWith("https://s3.amazonaws.com"));
        verify(s3Client, times(1)).putObject(eq(bucketName), anyString(), any(), any(ObjectMetadata.class));
    }

    @Test
    public void testUploadFile_IOException() throws IOException{

        String originalFileName="test.txt";
        when(file.getOriginalFilename()).thenReturn(originalFileName);
        when(file.getInputStream()).thenThrow(new IOException());

        assertThrows(IOException.class, ()->fileService.uploadFile(file));
    }

    @Test
    public void testGetObjectMetadata(){
        when(file.getContentType()).thenReturn("text/plain");
        when(file.getSize()).thenReturn(123L);

        ObjectMetadata metadata=fileService.getObjectMetadata(file);

        assertEquals("text/plain", metadata.getContentType());
        assertEquals(123L, metadata.getContentLength());
    }

    @Test
    public void testGenerateFileName(){
        String originalFileName="test.txt";
        when(file.getOriginalFilename()).thenReturn(originalFileName);

        String fileName=fileService.generateFileName(file);

        assertTrue(fileName.contains(originalFileName));
    }



}
