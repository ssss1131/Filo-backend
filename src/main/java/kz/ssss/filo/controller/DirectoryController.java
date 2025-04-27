package kz.ssss.filo.controller;

import kz.ssss.filo.dto.response.CreateDirectoryResponse;
import kz.ssss.filo.dto.response.FolderInfoResponse;
import kz.ssss.filo.dto.response.ObjectsInfoResponse;
import kz.ssss.filo.service.FolderService;
import kz.ssss.filo.util.PathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kz.ssss.filo.util.SecurityUtil.getAuthenticatedUserId;

@RequiredArgsConstructor
@RequestMapping("/api/directory")
@RestController
public class DirectoryController {

    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<?> createFolder(@RequestParam(name = "path") String path) {
        folderService.createFolder(getAuthenticatedUserId(), path);
        return ResponseEntity.ok().body(new CreateDirectoryResponse(PathUtil.getPath(path),
                PathUtil.getName(path)));
    }

    @GetMapping("/available")
    public ResponseEntity<?> availableFolders(@RequestParam(name = "path") String path) {
        List<FolderInfoResponse> folders = folderService.getAvailableDestinationFolders(getAuthenticatedUserId(), path);
        return ResponseEntity.ok().body(folders);

    }

}
