package kz.ssss.filo.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UploadFilesRequest {

    private Long userId;

    private String path;

    private List<MultipartFile> files;

}
