package com.DormitoryBack.domain.member.image.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validToken = "validToken";
        invalidToken = "invalidToken";
        userId = 1L;
        image = new Image();
        image.setImageBytes(new byte[]{1, 2, 3});
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
}
