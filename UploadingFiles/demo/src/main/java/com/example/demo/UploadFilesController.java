package com.example.demo;

import com.example.demo.storage.StorageFileNotFoundException;
import com.example.demo.storage.StorageServices;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class UploadFilesController {
    private final StorageServices storageServices;


    public UploadFilesController(StorageServices storageServices) {
        this.storageServices = storageServices;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException{

        model.addAttribute("files", storageServices.loadAll().map(
                path->MvcUriComponentsBuilder.fromMethodName(UploadFilesController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString()
        ).collect(Collectors.toList()));
        return "uploadForm";
    }

    @GetMapping("/files")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(String filename){
        Resource file=storageServices.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes){
        storageServices.store(file);
        redirectAttributes.addFlashAttribute("message", "You uploaded successfully "+file.getOriginalFilename());
        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException e){
        return ResponseEntity.notFound().build();
    }

}
