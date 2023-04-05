package com.mss.ged.controllers;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/download")
public class FileDownloadController {

    @GetMapping("")
    public ResponseEntity<InputStreamResource> downloadFile() throws IOException {
        // Get the file from the application folder
        File file = new File("Files-upload/2023/03/29/12/53/bc143af3-e959-4f80-bac2-4a5d14360257.bin");

        // Create an input stream resource from the file
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        // Set the headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.add(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

        // Create and return the response entity
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(org.springframework.http.MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

}
    
    