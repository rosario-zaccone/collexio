package org.collexio.utilities;

import org.collexio.domain.Item;
import org.collexio.domain.ItemPhoto;


import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.TreeSet;


public class SystemPhotoManager implements PhotoManager {

    private final String folderPath;

    public SystemPhotoManager(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getFolderPath() {
        return folderPath;
    }

    @Override
    public void addPhoto(String itemId, ItemPhoto photo) throws IOException { // only one photo a day
        String ext = photo.getPath().getFileName().toString().split("\\.")[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy-HH-mm");
        Files.copy(photo.getPath(), Paths.get(folderPath + itemId + "_" + photo.getTimestamp().format(formatter) + "." + ext));
    }

    @Override
    public Set<ItemPhoto> getPhotos(String itemId) {
        Set<ItemPhoto> photos = new TreeSet<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy-HH-mm");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(folderPath))) {
            for (Path file : stream) {
                if (file.toString().contains(itemId)) {
                    photos.add(new ItemPhoto(file, LocalDateTime.parse(file.toString().split("_")[1].split("\\.")[0], formatter)));
                }
            }
        } catch (IOException e) {
            System.err.println("Error while exploring the directory: " + e.getMessage());
        }
        return photos;
    }
}
