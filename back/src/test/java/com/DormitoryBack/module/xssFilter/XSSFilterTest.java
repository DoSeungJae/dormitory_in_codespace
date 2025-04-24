package com.DormitoryBack.module.xssFilter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;



import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class XSSFilterTest {

    @Test
    public void testFilterXSS(){
        String unsafeHtml="<script>alert('XSS')</script><b>안전한 텍스트</b> 일반 텍스트";
        String safeHtml=XSSFilter.filter(unsafeHtml);

        assertFalse(safeHtml.contains("<script>"));
        assertFalse(safeHtml.contains("</script>"));
        assertFalse(safeHtml.contains("alert('XSS')"));

        assertTrue(safeHtml.contains("<b>"));
        assertTrue(safeHtml.contains("</b>"));
        assertTrue(safeHtml.contains("안전한 텍스트"));
        
        assertTrue(safeHtml.contains("일반 텍스트"));
        log.info(safeHtml);
   

    }
}
