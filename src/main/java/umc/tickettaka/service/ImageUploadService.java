package umc.tickettaka.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
    //return S3 url
    String uploadImage(MultipartFile file) throws IOException;
}
