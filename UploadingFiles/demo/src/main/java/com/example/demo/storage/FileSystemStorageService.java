package com.example.demo.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageServices {
    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties storageProperties){
        this.rootLocation= Paths.get(storageProperties.getLocation());
    }

    @Override
    public void init() {
        try{
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage");
        }

    }

    @Override
    public void store(MultipartFile file){
        try{
            if(file.isEmpty()){
                throw new StorageException("Failed to store empty file "+file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
        }
        catch(IOException e){
            e.printStackTrace();
            throw new StorageException("Failed to store file "+file.getOriginalFilename());
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try{
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path->this.rootLocation.relativize(path));
        }
        catch(Exception e){
            throw new StorageException("Failed to read stored files: "+e);
        }
    }

    @Override
    public Path load(String fileName) {
        return this.rootLocation.resolve(fileName);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try{
            Path file=load(filename);
            Resource resource=new UrlResource(file.toUri());
            if(resource.isReadable() || resource.exists()) return resource;
            else throw new StorageException("Cannot read file "+filename);
        } catch (MalformedURLException e) {
            throw new StorageException("Failed to read stored file "+e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(this.rootLocation.toFile());
    }
}
