package src;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Answer implements Votable, Commentable {
    private final String id;
    private final String content;
    private final User author;
    private final Question question;
    private boolean isAccepted;
    private final Date creationDate;
    private final List<Comment> comments;
    private final List<Vote> votes;

    public Answer(User author, Question question, String content) {
        this.id = generateId();
        this.author = author;
        this.question = question;
        this.content = content;
        this.creationDate = new Date();
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
        return "ANS" + UUID.randomUUID();
    }

    // Getters
    public String getId() { return id; }
    public User getAuthor() { return author; }
    public Question getQuestion() { return question; }
    public String getContent() { return content; }
    public Date getCreationDate() { return creationDate; }
    public boolean isAccepted() { return isAccepted; }
}