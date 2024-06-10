package com.SWD.Order_Dish.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
@UtilityClass
public class S3Util {
    private final Logger LOGGER = LoggerFactory.getLogger(S3Util.class);

    @Value("${aws.bucketName}")
    private String bucketName ;
    private AmazonS3 s3;

    public String uploadFile(MultipartFile multipartFile) {
        LOGGER.info("Upload file to S3");
        File file;
        try {
            file = convertMulitpartToFile(multipartFile);
            String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            s3.putObject(new PutObjectRequest(bucketName, fileName, file));
            file.delete();
            return s3.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            LOGGER.error("Upload fail {}",e.getMessage());
        }
        return null;
    }

    public byte[] downloadFile(String fileName) {
        LOGGER.info("download file from S3");
        S3Object object = s3.getObject(bucketName, fileName);
        S3ObjectInputStream objectContent = object.getObjectContent();
        try{
            return IOUtils.toByteArray(objectContent);
        }catch (IOException e) {
            LOGGER.error("Download file fail {}",e.getMessage());
        }
        return null;
    }

    public boolean deleteFile(String fileName) {
        LOGGER.info("delete file from S3");
        try{
            s3.deleteObject(bucketName, fileName);
            return true;
        }catch (Exception e) {
            LOGGER.error("Delete file fail {}",e.getMessage());
            return false;
        }
    }

    private File convertMulitpartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
