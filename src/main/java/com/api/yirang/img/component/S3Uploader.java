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

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final static String TEMP_FILE_PATH = "./";
    private final AmazonS3Client amazonS3Client;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile){
        File convertFile = convert(multipartFile);
        String imgUrl = uploadS3(convertFile);
        removeTempFile(convertFile);
        return imgUrl;
    }

    private String uploadS3(File uploadFile){
        String fileName = uploadFile.getName();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, uploadFile);
        amazonS3Client.putObject(putObjectRequest);
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private File convert(MultipartFile multipartFile){
        try{
        File convertFile = new File(TEMP_FILE_PATH + multipartFile.getOriginalFilename());
        if (convertFile.createNewFile()){
            FileOutputStream fos = new FileOutputStream(convertFile);
            fos.write(multipartFile.getBytes());
            return convertFile;
        }
        throw new Exception();
        }
        catch (Exception e){
            throw new FileConvertException();
            }
        }

    private void removeTempFile(File targetFile) {
        if (targetFile.delete()){
            return;
        }
    }
}
