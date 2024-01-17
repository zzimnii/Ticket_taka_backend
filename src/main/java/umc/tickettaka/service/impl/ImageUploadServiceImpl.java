package umc.tickettaka.service.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import umc.tickettaka.service.ImageUploadService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageUploadServiceImpl implements ImageUploadService {

    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        String fileName = file.getOriginalFilename();
        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
    }
}
