package kz.ssss.filo.dto.request;

import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Value
public class UploadFileRequest {

    private Long userId;
    private String path;
    private List<MultipartFile> files;

}
