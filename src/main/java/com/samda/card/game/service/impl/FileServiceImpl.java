package com.samda.card.game.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import com.samda.card.game.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Service
@NoArgsConstructor
@Getter
@Setter
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException{
        String fileName = file.getOriginalFilename();
        String randomId = UUID.randomUUID().toString();
        String newFileName = randomId.concat(fileName.substring(fileName.lastIndexOf(".")));
        String filePath = path+File.separator+newFileName;
        File newFile = new File(path);
        if(!newFile.exists()) {
            newFile.mkdir();
        }
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return newFileName;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException{
        String fullPath = path + File.separator + fileName;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}