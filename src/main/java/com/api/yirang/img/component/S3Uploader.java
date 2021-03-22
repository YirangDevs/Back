package com.api.yirang.img.component;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.api.yirang.img.exception.FileConvertException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile){
        System.out.println("In S3Uplodaer, MultiparFile: " + multipartFile.getOriginalFilename());
        try {
            File convertFile = convert(multipartFile);
            String imgUrl = uploadS3(convertFile);
            removeTempFile(convertFile);
            return imgUrl;
        }
        catch (Exception e){
            e.printStackTrace(System.out);
            throw new FileConvertException();
        }
    }

    private String uploadS3(File uploadFile){
        String fileName = uploadFile.getName();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, uploadFile);
        amazonS3Client.putObject(putObjectRequest);
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        System.out.println("I'm Trying to make File!");
        File convertFile = new File(System.getProperty("java.io.tmpdir") +
                                    System.getProperty("file.separator") +
                                    multipartFile.getOriginalFilename());
        System.out.println("I Made ConvertFile!: " + convertFile.getName());
        if (convertFile.createNewFile()) {
            System.out.println("We did make createNew File!!");
            FileOutputStream fos = new FileOutputStream(convertFile);
            System.out.println("FileOutPutStream fos!!");
            fos.write(multipartFile.getBytes());
            System.out.println("fos write success!!");
        }
        return convertFile;
    }

    private void removeTempFile(File targetFile) {
        if (targetFile.delete()){
            return;
        }
    }
}
