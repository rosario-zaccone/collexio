package org.collexio.domain;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.collexio.utilities.Utilities;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemCollection <T extends Item> {
    private final String id;
    private String name;
    private final List<T> data;

    public ItemCollection(String id, String name) {
        if (!Utilities.validateId("C-", id))
            throw new IllegalArgumentException("Id must be in the format C-###, where ### is a natural number");
        this.id = id;
        this.name = name;
        this.data = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<T> getData() {
        return Collections.unmodifiableList(data); // no deep copy, to be improved
    }

    public void addItem(T item) {
        data.add(item);
    }

    public void removeItem(T item) {
        data.remove(item);
    }

    public int getTotalQuantity() {
        return data.stream().map(e -> e.getQuantity()).mapToInt(Integer::intValue).sum();
    }

    public void buildPhoto() throws IOException {
        String outputPath = "images/collections/all_" + id;
        List<Path> imagePaths = data.stream().map(e -> e.getPhoto().getPath()).toList();
        List<String> labels = data.stream().map(e -> getName()).toList();
        String title = "Collection: " + name;
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.LETTER);
        doc.addPage(page);
        PDPageContentStream content = new PDPageContentStream(doc, page);

        float margin = 50;
        float yStart = page.getMediaBox().getHeight() - margin;
        float xStart = margin;
        int columns = 3;
        float imageWidth = 150;
        float imageHeight = 100;
        float labelHeight = 15;
        float cellHeight = imageHeight + labelHeight + 10;
        float xSpacing = 20;
        float ySpacing = 30;

        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 20);
        content.newLineAtOffset(margin, yStart);
        content.showText(title);
        content.endText();

        float y = yStart - 40;
        int count = 0;
        for (int i = 0; i < imagePaths.size(); i++) {
            if (count > 0 && count % columns == 0) {
                y -= cellHeight + ySpacing;
                xStart = margin;
            }

            PDImageXObject image = PDImageXObject.createFromFile(String.valueOf(imagePaths.get(i)), doc);
            content.drawImage(image, xStart, y - imageHeight, imageWidth, imageHeight);

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(xStart, y - imageHeight - 12);
            content.showText(labels.get(i));
            content.endText();

            xStart += imageWidth + xSpacing;
            count++;
        }

        content.close();
        doc.save(outputPath);
        doc.close();
    }
}
