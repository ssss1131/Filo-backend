package kz.ssss.filo.controller;

import kz.ssss.filo.dto.response.ObjectsInfoResponse;
import kz.ssss.filo.exception.QuotaExceededException;
import kz.ssss.filo.service.FileService;
import kz.ssss.filo.service.FolderService;
import kz.ssss.filo.service.ResourceService;
import kz.ssss.filo.service.UserQuotaService;
import kz.ssss.filo.util.PathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static kz.ssss.filo.util.SecurityUtil.getAuthenticatedUserId;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final FileService fileService;
    private final FolderService folderService;
    private final ResourceService resourceService;
    private final UserQuotaService userQuotaService;

    @GetMapping
    public ResponseEntity<?> getFiles(@RequestParam(name = "path", required = false, defaultValue = "") String path) {
        return ResponseEntity.ok(resourceService.getResourcesInFolder(getAuthenticatedUserId(), path));
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam(name = "path", required = false, defaultValue = "") String path,
                                        @RequestParam("files") List<MultipartFile> files) {

        Long userId = getAuthenticatedUserId();

        long totalNewBytes = files.stream()
                .mapToLong(MultipartFile::getSize)
                .sum();
        if (userQuotaService.willExceedQuota(userId, totalNewBytes)) {
            throw new QuotaExceededException("Quota exceeded");
        }

        files.forEach(file ->
                fileService.upload(userId, path, file)
        );
        return ResponseEntity
                .status(CREATED)
                .body(resourceService.getResourcesInFolder(userId, path));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam(name = "path") String path) {
        resourceService.delete(getAuthenticatedUserId(), path);
        return ResponseEntity
                .status(NO_CONTENT)
                .build();
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
            String encodedFileName = URLEncoder.encode(PathUtil.getName(path), StandardCharsets.UTF_8);
            String contentDisposition = "attachment; filename*=UTF-8''" + encodedFileName;

            return ResponseEntity.ok()
                    .header(CONTENT_DISPOSITION, contentDisposition)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(stream);

        } else {
            StreamingResponseBody stream = outputStream ->
                    folderService.downloadFolder(getAuthenticatedUserId(), path, outputStream);

            String folderName = PathUtil.getName(path) + ".zip";
            String encodedFolderName = URLEncoder.encode(folderName, StandardCharsets.UTF_8);
            String contentDisposition = "attachment; filename*=UTF-8''" + encodedFolderName;
            return ResponseEntity.ok()
                    .header(CONTENT_DISPOSITION, contentDisposition)
                    .contentType(MediaType.valueOf("application/zip"))
                    .body(stream);
        }
    }

    @GetMapping("/move")
    public ResponseEntity<?> move(@RequestParam(name = "from") String from, @RequestParam(name = "to") String to){
        List<ObjectsInfoResponse> resources = resourceService.move(getAuthenticatedUserId(), from, to);
        return ResponseEntity.ok()
                .body(resources);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(name = "query") String query){
        List<?> result = resourceService.search(getAuthenticatedUserId(), query);
        return ResponseEntity.ok().body(result);
    }

}
