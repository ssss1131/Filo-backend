package kz.ssss.filo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.ssss.filo.dto.response.CreateDirectoryResponse;
import kz.ssss.filo.dto.response.FolderInfoResponse;
import kz.ssss.filo.service.FolderService;
import kz.ssss.filo.util.PathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kz.ssss.filo.util.SecurityUtil.getAuthenticatedUserId;

@Tag(name = "Directory", description = "Operations for creating and getting available folders to move")
@RequiredArgsConstructor
@RequestMapping("/api/directory")
@RestController
public class DirectoryController {

    private final FolderService folderService;

    @Operation(
            summary = "Create folder",
            description = "Creates a placeholder file ('.keep') at the specified path to represent a new folder",
            security = @SecurityRequirement(name = "cookieAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Folder successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateDirectoryResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid folder path",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Folder already exists",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<?> createFolder(@RequestParam(name = "path") String path) {
        folderService.createFolder(getAuthenticatedUserId(), path);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateDirectoryResponse(PathUtil.getPath(path),
                PathUtil.getName(path)));
    }

    @Operation(
            summary = "List available folders",
            description = "Returns folders where an object can be moved; excludes the source folder and any folder where an object with the same name already exists",
            security = @SecurityRequirement(name = "cookieAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "list of available folders",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FolderInfoResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid path",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not Authorized",
                    content = @Content
            )
    })
    @GetMapping("/available")
    public ResponseEntity<?> availableFolders(@RequestParam(name = "path") String path) {
        List<FolderInfoResponse> folders = folderService.getAvailableDestinationFolders(getAuthenticatedUserId(), path);
        return ResponseEntity.ok().body(folders);

    }

}
