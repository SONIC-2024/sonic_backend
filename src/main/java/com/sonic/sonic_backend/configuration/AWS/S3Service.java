package com.sonic.sonic_backend.configuration.AWS;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sonic.sonic_backend.domain.Profile.repository.MemberProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final MemberProfileRepository memberProfileRepository;
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //post에 첨부되는 이미지 업로드
    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String createdFilename = createFileName(originalFilename);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, createdFilename, multipartFile.getInputStream(), metadata);
        return createdFilename;
    }

    //프로필 이미지 업로드
    public String saveProfileFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String createdFilename = createFileName(originalFilename);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, createdFilename, multipartFile.getInputStream(), metadata);

        return createdFilename;
    }

    //삭제
    public void delete(String url) {
        amazonS3.deleteObject(bucket, url);
    }

    //랜덤이름 생성
    public String createFileName(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        return UUID.randomUUID().toString()+fileExtension;
    }

    //실제 주소 얻기
    public String getFullUrl(String key) {
        return amazonS3.getUrl(bucket,key).toString();
    }
}
