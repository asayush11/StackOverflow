package src;

import java.util.Date;
import java.util.UUID;

public class Comment {
    private final String id;
    private final String content;
    private final User author;
    private final Date creationDate;

    public Comment(User author, String content) {
        this.id = generateId();
        this.author = author;
        this.content = content;
        this.creationDate = new Date();
    }

    private String generateId() {
        return "COM" + UUID.randomUUID();
    }

    // Getters
    public String getId() { return id; }
    public User getAuthor() { return author; }
    public String getContent() { return content; }
    public Date getCreationDate() { return creationDate; }
}