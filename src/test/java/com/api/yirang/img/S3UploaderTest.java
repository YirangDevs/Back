package com.api.yirang.img;


import com.api.yirang.img.component.S3Uploader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class S3UploaderTest {


    @Autowired
    S3Uploader s3Uploader;


    @Test
    public void 파일_업로드(){
        String data = "a,b,c";
        MultipartFile multipartFile = new MockMultipartFile("files", "abc.csv", "text/plain", data.getBytes(StandardCharsets.UTF_8));
//        System.out.println("URk: " + s3Uploader.upload(multipartFile) );
    }
}
