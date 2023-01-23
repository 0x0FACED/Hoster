package ru.microhost.hoster.controllers;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.microhost.hoster.repos.FilesRepository;
import ru.microhost.hoster.services.FileUploadService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {
    private FileUploadService fileUploadService = new FileUploadService();
    private FilesRepository storageService = new FileUploadService();

    @GetMapping("/files")
    public String files(String q, Model model) throws IOException {
        storageService.loadFilesToMap();
        if (q == null) {
            model.addAttribute("files", storageService.getFilesAsArray().map(
                            path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                    "serveFile", path.getFileName().toString()).build().toUri().toString())
                    .collect(Collectors.toList()));
        } else {
            model.addAttribute("files", storageService.getFilesAsArray(q).map(
                            path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                    "serveFile", path.getFileName().toString()).build().toUri().toString())
                    .collect(Collectors.toList()));
        }
        return "files";
    }

    @PostMapping("/files")
    public String addFile(MultipartFile file, Model model) throws IOException {
        if (!file.isEmpty()) {
            fileUploadService.addFile(file, model);
        }
        model.addAttribute("files", storageService.getFilesAsArray().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));
        return "files";
    }

    @GetMapping("/files/{fileID:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String fileID) {
        Resource file = storageService.loadAsResource(fileID);
        ResponseEntity<Resource> responseEntity = ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + storageService.getRealName(fileID) + "\"").body(file);
        return responseEntity;
    }


}
