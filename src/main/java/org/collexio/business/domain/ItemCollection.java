package org.collexio.business.domain;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemCollection {
    private final Long id;
    private String name;
    private final List<Item> data;

    public ItemCollection(Long id, String name) {
        if (id != null && id <= 0)
            throw new IllegalArgumentException("Id must be positive or null");
        if (name.isEmpty())
            throw new IllegalArgumentException("Name can't be empty");
        this.id = id;
        this.name = name;
        this.data = new ArrayList<>();
    }

    public ItemCollection(String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Item> getData() {
        List<Item> res = new ArrayList<>();
        for (Item item: data)
            res.add((Item) item.copy());
        return res;
    }

    public void addItem(Item item) { //TODO: deep copy
        data.add(item.copy());
    }

    public void removeItem(Item item) {
        data.remove(item);
    }

    public int getTotalQuantity() {
        return data.stream().map(e -> e.getQuantity()).mapToInt(Integer::intValue).sum();
    }

    public String buildPhoto() throws IOException {
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
        return outputPath;
    }

    @Override
    public String toString() {
        return "ItemCollection{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", data=" + data +
                '}';
    }

    public String toStringNoId() {
        return "ItemCollection{" +
                "name='" + name + '\'' +
                ", data=" +  data.stream()
                .map(Item::toStringNoId)
                .collect(Collectors.joining(", ")) + +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCollection that = (ItemCollection) o;
        return Objects.equals(name, that.name) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data);
    }
}
