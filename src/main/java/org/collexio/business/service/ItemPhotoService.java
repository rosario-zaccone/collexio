package org.collexio.business.service;

import org.collexio.AppConfig;
import org.collexio.business.domain.ItemPhoto;
import org.collexio.persistence.dao.ItemPhotoDAO;
import org.collexio.persistence.entity.ItemPhotoEntity;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ItemPhotoService {
    private final ItemPhotoDAO dao;

    public ItemPhotoService(ItemPhotoDAO dao) {
        this.dao = dao;
    }

    public ItemPhoto add(ItemPhoto photo, Long itemId) throws SQLException, IOException {

        Path target = AppConfig.getImagesDir()
                .resolve(itemId.toString() + "." + photo.getPath().getFileName().toString().replaceFirst(".*\\.", ""));
        Files.copy(photo.getPath(), target, StandardCopyOption.REPLACE_EXISTING);
        // db
        try {
            ItemPhoto p = new ItemPhoto(photo.getId(),  target, photo.getDate());
            return ItemPhoto.fromEntity(dao.add(p.toEntity(), itemId));
        } catch (SQLException e) {
            // delete file previous added in filesystem
            Files.deleteIfExists(target);
            throw e;
        }
    }

    public ItemPhoto getByItemId(Long id) throws SQLException {
        Optional<ItemPhotoEntity> res = dao.getByItemId(id);
        if (res.isEmpty())
            throw new NoSuchElementException("ItemPhoto not found for item id: " + id);
        return ItemPhoto.fromEntity(res.get());
    }

    public ItemPhoto update(ItemPhoto photo, Long itemId) throws SQLException, IOException {
        Path target = AppConfig.getImagesDir()
                .resolve(itemId.toString() + "." + photo.getPath().getFileName().toString().replaceFirst(".*\\.", ""));
        Files.copy(photo.getPath(), target, StandardCopyOption.REPLACE_EXISTING);

        try {
            ItemPhoto p = new ItemPhoto(photo.getId(),  target, photo.getDate());
            dao.update(p.toEntity());
            return p;
        } catch (SQLException e) {
            // delete file previous added in filesystem
            Files.deleteIfExists(target);
            throw e;
        }
    }

    public void deleteByItemId(Long id) throws SQLException {
        dao.deleteByItemId(id);
    }

}
