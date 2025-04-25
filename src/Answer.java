package src;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Answer implements Votable, Commentable {
    private final String id;
    private StringBuilder content;
    private final User author;
    private boolean isAccepted;
    private final LocalDate creationDate;
    private final List<Comment> comments;
    private final List<Vote> votes;

    public Answer(User author, StringBuilder content) {
        this.id = generateId();
        this.author = author;
        this.content = content;
        this.creationDate = LocalDate.now();
        this.votes = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.isAccepted = false;
    }

    @Override
    public void vote(User user, int value) {
        if (value != 1 && value != -1) {
            System.out.println("Vote value must be either 1 or -1");
            return;
        }
        votes.removeIf(v -> v.getUser().equals(user));
        votes.add(new Vote(user, value));
        author.updateReputation(value * 10);  // +10 for upvote, -10 for downvote
    }

    @Override
    public int getVoteCount() {
        return votes.stream().mapToInt(Vote::getValue).sum();
    }

    @Override
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    @Override
    public List<Comment> getComments() {
        return comments;
    }

    public void markAsAccepted() {
        if (isAccepted) {
            System.out.println("This answer is already accepted");
            return;
        }
        isAccepted = true;
        author.updateReputation(15);  // +15 reputation for accepted answer
    }

    private String generateId() {
        return "ANS" + UUID.randomUUID().toString().substring(0,8);
    }

    // Getters
    public String getId() { return id; }
    public User getAuthor() { return author; }
    public StringBuilder getContent() { return content; }
    public LocalDate getCreationDate() { return creationDate; }
    public boolean isAccepted() { return isAccepted; }
    public void updateContent(StringBuilder content) {
        this.content = content;
    }
    public void updateComment(Comment comment, StringBuilder content) {
        comments.stream()
                .filter(c -> c.getId().equals(comment.getId()))
                .findFirst()
                .ifPresentOrElse(c -> c.updateContent(content),
                        () -> System.out.println("Comment not found"));
    }
}