package com.tickerBell.global.config;

import com.amazonaws.services.s3.AmazonS3Client;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.net.MalformedURLException;
import java.net.URL;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Profile("test")
@Configuration
public class MockS3Config extends S3Config {

    @Bean
    @Primary
    @Override
    public AmazonS3Client amazonS3Client() throws MalformedURLException {
        AmazonS3Client mock = Mockito.mock(AmazonS3Client.class);

        when(mock.getUrl(anyString(), anyString())).thenReturn(new URL("https://example.com/yourBucketName/yourFileName"));

        return mock;
    }
}
