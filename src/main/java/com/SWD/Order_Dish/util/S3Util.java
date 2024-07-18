package com.SWD.Order_Dish.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Component
public class S3Util {
    private static final Logger LOGGER = LoggerFactory.getLogger(S3Util.class);

    @Value("${aws.bucketName}")
    private static String bucketName;
    private static AmazonS3 s3;

    @Autowired
    public S3Util(AmazonS3 s3, @Value("${aws.bucketName}") String bucketName) {
        S3Util.s3 = s3;
        S3Util.bucketName = bucketName;
    }

    public static String uploadFile(MultipartFile multipartFile) {
        LOGGER.info("Upload file to S3");
        File file;
        try {
            file = convertMulitpartToFile(multipartFile);
            String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            s3.putObject(new PutObjectRequest(bucketName, fileName, file));
            file.delete();
            LOGGER.info("Upload success");
            return s3.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            LOGGER.error("Upload fail {}", e.getMessage());
        }
        return null;
    }

    public static byte[] downloadFile(String fileName) {
        LOGGER.info("Download file from S3");
        S3Object object = s3.getObject(bucketName, fileName);
        S3ObjectInputStream objectContent = object.getObjectContent();
        try {
            return IOUtils.toByteArray(objectContent);
        } catch (IOException e) {
            LOGGER.error("Download file fail {}", e.getMessage());
        }
        return null;
    }

    public static boolean deleteFile(String fileName) {
        LOGGER.info("Delete file from S3");
        try {
            s3.deleteObject(bucketName, fileName);
            return true;
        } catch (Exception e) {
            LOGGER.error("Delete file fail {}", e.getMessage());
            return false;
        }
    }

    private static File convertMulitpartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }
}


