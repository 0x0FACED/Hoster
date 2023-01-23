package ru.microhost.hoster.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.microhost.hoster.exceptions.StorageException;
import ru.microhost.hoster.exceptions.StorageFileNotFoundException;
import ru.microhost.hoster.repos.FilesRepository;
import ru.microhost.hoster.storage.StorageProperties;
import org.springframework.util.FileSystemUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileUploadService implements FilesRepository {
    private StorageProperties storageProperties = new StorageProperties();
    private static HashMap<String, String> storageRealName = new HashMap<>();

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public void loadFilesToMap() throws IOException {
        /*StorageProperties userStorage = new StorageProperties(getCurrentUsername());
        File uploadDir = new File(userStorage.getLocation());
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        Path path = Path.of(userStorage.getLocation());*/
        Path path = Path.of(storageProperties.getLocation());
        List<Path> files;
        try (Stream<Path> walk = Files.walk(path)) {
            files = walk
                    .filter(Files::isRegularFile)   // is a file
                    .collect(Collectors.toList());
        }
        if (files.isEmpty()) {
            return;
        }
        for (Path x : files) {
            String[] filename = x.getFileName().toString().split("_-_-_", 2);

            storageRealName.put(filename[0], filename[1]);
        }
    }

    @Override
    public void addFile(MultipartFile file, Model model) throws IOException {
        /*storageProperties = new StorageProperties(getCurrentUsername());*/
        File uploadDir = new File(storageProperties.getLocation());
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        byte[] fileBytes = file.getBytes();
        String uuidFile = UUID.randomUUID().toString();


        String finalOriginalName = replaceRussianToEnglish(file.getOriginalFilename());
        BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(uploadDir + "/" + uuidFile + "_-_-_" + finalOriginalName));
        stream.write(fileBytes);
        storageRealName.put(uuidFile, String.valueOf(finalOriginalName));
        stream.close();
    }

    private String replaceRussianToEnglish(String filename) {

        String english = "abvgdeejziyklmnoprstufhc4ss'i'euj";

        String russian = "абвгдеёжзийклмнопрстуфхцчшщьыъэюя";

        StringBuilder finalOriginalName = new StringBuilder();
        for (int i = 0; i < Objects.requireNonNull(filename).length(); ++i){
            char word = filename.toLowerCase().charAt(i);
            String word1 = String.valueOf(word);
            if (russian.contains(word1)) {
                finalOriginalName.append(english.charAt(russian.indexOf(word1)));
            } else {
                finalOriginalName.append(filename.charAt(i));
            }
        }

        return String.valueOf(finalOriginalName);
    }

    @Override
    public Stream<Path> getFilesAsArray() throws IOException {
        try {
            return Files.walk(Path.of(storageProperties.getLocation()), 1)
                    .filter(path -> !path.equals(Path.of(storageProperties.getLocation())))
                    .map(path -> Path.of(storageProperties.getLocation()).relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Stream<Path> getFilesAsArray(String filename) throws IOException {
        try {
            filename = filename.toLowerCase();
            String finalFilename = filename;
            return Files.walk(Path.of(storageProperties.getLocation()), 1)
                    .filter(Files::isRegularFile)
                    .filter(Files::isReadable)
                    .filter(path -> !path.equals(Path.of(storageProperties.getLocation())))
                    .filter(path -> path.getFileName().toString().substring(41).split("\\.")[0].toLowerCase().contains(finalFilename))
                    .map(path -> Path.of(storageProperties.getLocation()).relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return Path.of(storageProperties.getLocation()).resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public String getRealName(String fileID) {
        String[] id = fileID.split("_", 0);
        return storageRealName.get(id[0]);
    }

    @Override
    public void init() {
        storageRealName = new HashMap<>();
    }
}
