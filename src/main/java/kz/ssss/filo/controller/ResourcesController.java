package kz.ssss.filo.controller;

import kz.ssss.filo.dto.request.CreateFolderRequestDto;
import kz.ssss.filo.dto.request.UploadFileRequestDto;
import kz.ssss.filo.service.FileService;
import kz.ssss.filo.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourcesController {

    private final FileService fileService;
    private final FolderService folderService;

    @PostMapping("/upload")
    public String uploadFile(@ModelAttribute UploadFileRequestDto fileRequestDto,
                             RedirectAttributes redirectAttributes) {
        try {
            fileRequestDto.getFiles().forEach(file ->
                    fileService.upload(fileRequestDto.getUserId(), fileRequestDto.getPath(), file)
            );
            redirectAttributes.addFlashAttribute("successMessage", "Files uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error uploading files: " + e.getMessage());
        }
        return "redirect:/?path=" + fileRequestDto.getPath();
    }


    @PostMapping("/create-folder")
    public String createFolder(@ModelAttribute CreateFolderRequestDto createFolderRequestDto,
                               RedirectAttributes redirectAttributes) {
        try {
            folderService.createFolder(createFolderRequestDto);
            redirectAttributes.addFlashAttribute("successMessage", "Folder created successfully!");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating folder: " + e.getMessage());
        }
        return "redirect:/?path=" + createFolderRequestDto.getPath();
    }


}
