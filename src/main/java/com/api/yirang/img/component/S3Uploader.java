package com.api.yirang.img.component;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.api.yirang.img.exception.FileConvertException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
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

    public String upload(Long userId, MultipartFile multipartFile){
        try {
            File convertFile = convert(multipartFile);
            String imgUrl = uploadS3(userId, convertFile);
            removeTempFile(convertFile);
            return imgUrl;
        }
        catch (Exception e){
            e.printStackTrace(System.out);
            throw new FileConvertException();
        }
    }

    private String uploadS3(Long userId, File uploadFile){
        String fileName = userId.toString() + "." + FilenameUtils.getExtension(uploadFile.getName());
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, uploadFile);
        amazonS3Client.putObject(putObjectRequest);
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        // AWS EC2는 creatNewFile이 금지가 되어있어서, System.getProperty로 값을 얻어야함
        File convertFile = new File(System.getProperty("java.io.tmpdir") +
                                    System.getProperty("file.separator") +
                                    multipartFile.getOriginalFilename());
        if (convertFile.createNewFile()) {
            FileOutputStream fos = new FileOutputStream(convertFile);
            fos.write(multipartFile.getBytes());
        }
        return convertFile;
    }

    private void removeTempFile(File targetFile) {
        if (targetFile.delete()){
            return;
        }
    }
}
