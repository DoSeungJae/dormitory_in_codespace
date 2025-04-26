package com.DormitoryBack.module.xssFilter;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class XSSFilter {
    
    public static String filter(String value){
        if(value==null){
            return null;
        }
    
        PolicyFactory policy=Sanitizers.FORMATTING.and(Sanitizers.LINKS);
        String safeHtml=policy.sanitize(value);

        return safeHtml;
        
    }
}
