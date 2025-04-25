package src;

import java.time.LocalDate;
import java.util.UUID;

public class Comment {
    private final String id;
    private StringBuilder content;
    private final User author;
    private final LocalDate creationDate;

    public Comment(User author, StringBuilder content) {
        this.id = generateId();
        this.author = author;
        this.content = content;
        this.creationDate = LocalDate.now();
    }

    private String generateId() {
        return "COM" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    // Getters
    public String getId() { return id; }
    public User getAuthor() { return author; }
    public StringBuilder getContent() { return content; }
    public LocalDate getCreationDate() { return creationDate; }

    public void updateContent(StringBuilder content) {
        this.content = content;
    }
}