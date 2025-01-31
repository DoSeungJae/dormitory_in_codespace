package com.DormitoryBack.domain.member.image.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.dto.ImageDTO;
import com.DormitoryBack.domain.member.domain.entity.Image;
import com.DormitoryBack.domain.member.domain.repository.ImageRepository;
import com.DormitoryBack.domain.member.domain.service.UserService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ImageServiceTest {

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private ImageRepository imageRepository;

    @Autowired
    private UserService userService;

    private String validToken;
    private String invalidToken;
    private Long userId;
    private Image image;
    private Image image2;
    private MultipartFile imageFile;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validToken = "validToken";
        invalidToken = "invalidToken";
        userId = 1L;
        image = new Image();
        image.setImageBytes(new byte[]{1, 2, 3});

        imageFile=mock(MultipartFile.class);
        image2=Image.builder()
            .userId(userId)
            .imageBytes(new byte[]{1,2,3})
            .contentType("image/jpeg")
            .fileName("test.jpg")
            .build();
        
    }

    @Test
    public void testGetProfileImage_ValidToken() {
        when(tokenProvider.validateToken(validToken)).thenReturn(true);
        when(tokenProvider.getUserIdFromToken(validToken)).thenReturn(userId);
        when(imageRepository.findByUserId(userId)).thenReturn(image);

        ImageDTO result = userService.getProfileImage(validToken);

        assertNotNull(result);
        assertArrayEquals(new byte[]{1, 2, 3}, result.getImageBytes());
        assertEquals(MediaType.IMAGE_JPEG, result.getHeaders().getContentType());
    }

    @Test
    public void testGetProfileImage_InvalidToken() {
        when(tokenProvider.validateToken(invalidToken)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getProfileImage(invalidToken);
        });

        assertEquals("InvalidToken", exception.getMessage());
    }

    @Test
    public void testGetProfileImage_ImageNotFound() {
        when(tokenProvider.validateToken(validToken)).thenReturn(true);
        when(tokenProvider.getUserIdFromToken(validToken)).thenReturn(userId);
        when(imageRepository.findByUserId(userId)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getProfileImage(validToken);
        });

        assertEquals("ImageError", exception.getMessage());
    }

    @Test
    public void testSaveProfileImage_ValidToken() throws IOException{
        when(tokenProvider.validateToken(validToken)).thenReturn(true);
        when(tokenProvider.getUserIdFromToken(validToken)).thenReturn(userId);
        when(imageFile.getBytes()).thenReturn(new byte[]{1,2,3});
        when(imageFile.getOriginalFilename()).thenReturn("test.jpg");
        when(imageFile.getContentType()).thenReturn("image/jpeg");

        userService.saveProfileImage(imageFile, validToken);

        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    public void testSaveProfileImage_InvalidToken() throws IOException{
        when(tokenProvider.validateToken(invalidToken)).thenReturn(false);

        RuntimeException exception=assertThrows(RuntimeException.class, () ->{
            userService.saveProfileImage(imageFile, invalidToken);
        });
        assertEquals("InvalidToken",exception.getMessage());
    }

    @Test
    public void testSaveProfileImage_IOException() throws IOException {
        when(tokenProvider.validateToken(validToken)).thenReturn(true);
        when(tokenProvider.getUserIdFromToken(validToken)).thenReturn(userId);
        when(imageFile.getBytes()).thenThrow(new IOException());

        assertDoesNotThrow(()->userService.saveProfileImage(imageFile, validToken));
        verify(imageRepository,never()).save(any(Image.class));
    }
    
}
