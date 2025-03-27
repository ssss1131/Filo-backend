package kz.ssss.filo.controller;

import kz.ssss.filo.service.FileService;
import kz.ssss.filo.service.FolderService;
import kz.ssss.filo.util.PathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.util.List;

import static kz.ssss.filo.util.SecurityUtil.getAuthenticatedUserId;

@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
public class ResourcesController {

    private final FileService fileService;
    private final FolderService folderService;

    @GetMapping
    public ResponseEntity<?> getFiles(@RequestParam(name = "path", required = false, defaultValue = "") String path) {
        return ResponseEntity.ok(fileService.getObjectInfoInFolder(getAuthenticatedUserId(), path));
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam(name = "path", required = false, defaultValue = "") String path,
                                        @RequestParam("files") List<MultipartFile> files) {
        files.forEach(file ->
                fileService.upload(getAuthenticatedUserId(), path, file)
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fileService.getObjectInfoInFolder(getAuthenticatedUserId(), path));
    }

    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> download(
            @RequestParam(name = "path", required = false, defaultValue = "") String path) {
        if (PathUtil.isFile(path)) {
            Resource resource = fileService.downloadFile(getAuthenticatedUserId(), path);
            StreamingResponseBody stream = outputStream -> {
                try (InputStream is = resource.getInputStream()) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            };
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; name=\"" + PathUtil.getName(path) + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(stream);
        } else {
            StreamingResponseBody stream = outputStream ->
                    folderService.downloadFolder(getAuthenticatedUserId(), path, outputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; name=\"" + PathUtil.getName(path) + ".zip\"")
                    .contentType(MediaType.valueOf("application/zip"))
                    .body(stream);
        }
    }


}
