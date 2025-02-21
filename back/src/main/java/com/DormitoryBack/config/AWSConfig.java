package com.DormitoryBack.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketCrossOriginConfiguration;
import com.amazonaws.services.s3.model.CORSRule;

@Configuration
public class AWSConfig {

    @Value("${iamAccessKey}")
    private String iamAccessKey;

    @Value("${iamSecretKey}")
    private String iamSecretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${allowedOrigin1}")
    private String allowedOrigin1;

    @Value("${allowedOrigin2}")
    private String allowedOrigin2;

    @Bean
    public AmazonS3 amazonS3(){
        BasicAWSCredentials awsCredentials=new BasicAWSCredentials(iamAccessKey, iamSecretKey);

        AmazonS3 s3Client=AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .build();
        
        configureCors(s3Client);

        return s3Client;
    }
    
    public void configureCors(AmazonS3 s3Client){

        List<String> allowedOrigins=new ArrayList<>();
        allowedOrigins.add(allowedOrigin1);
        allowedOrigins.add(allowedOrigin2);

        List<CORSRule.AllowedMethods> rule1AM=new ArrayList<>();
        rule1AM.add(CORSRule.AllowedMethods.GET);

        CORSRule rule1=new CORSRule()
            .withId("CORSRule1")
            .withAllowedMethods(rule1AM)
            .withAllowedOrigins(allowedOrigins);

        List<CORSRule> rules=new ArrayList<>();
        rules.add(rule1);

        BucketCrossOriginConfiguration configuration=new BucketCrossOriginConfiguration();
        configuration.setRules(rules);

        s3Client.setBucketCrossOriginConfiguration(bucketName, configuration);
    }

    
    
}
