package utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@UtilityClass
public class FileDownloader {
    public String downloadFileToTemp(String fileUrl, String prefix, String suffix) {
        try (InputStream in = URI.create(fileUrl).toURL().openStream()) {
            Path tempFile = Files.createTempFile(prefix, suffix);
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            tempFile.toFile().deleteOnExit();
            return tempFile.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(ExceptionMessages.FILE_DOWNLOAD, e);
        }
    }

}
