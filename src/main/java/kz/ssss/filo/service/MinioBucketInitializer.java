package kz.ssss.filo.service;

import kz.ssss.filo.repository.MinioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MinioBucketInitializer {

    private final MinioRepository minioRepository;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(){
        minioRepository.createBucket(bucketName);
    }

}
