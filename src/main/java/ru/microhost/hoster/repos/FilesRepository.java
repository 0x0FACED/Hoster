package ru.microhost.hoster.repos;

import org.springframework.core.io.Resource;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FilesRepository {
    void addFile(MultipartFile file, Model model) throws IOException;

    Stream<Path> getFilesAsArray() throws IOException;
    Stream<Path> getFilesAsArray(String filename) throws IOException;
    void loadFilesToMap() throws IOException;

    /*Path load(String filename);*/

    Path load(String filename);

    Resource loadAsResource(String filename);

    String getRealName(String fileID);

    void init();
}
