package src;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Question implements Votable, Commentable {
    private final String id;
    private final String title;
    private StringBuilder content;
    private final User author;
    private final LocalDate creationDate;
    private final List<Answer> answers;
    private final List<Comment> comments;
    private final List<Tag> tags;
    private final List<Vote> votes;

    public Question(User author, String title, StringBuilder content, List<String> tagNames) {
        this.id = generateId();
        this.author = author;
        this.title = title;
        this.content = content;
        this.creationDate = LocalDate.now();
        this.answers = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.votes = new ArrayList<>();
        this.comments = new ArrayList<>();
        tagNames
                .forEach(s -> this.tags.add(new Tag(s)));
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    @Override
    public void vote(User user, int value) {
        if (value != 1 && value != -1) {
            System.out.println("Vote value must be either 1 or -1");
            return;
        }
        votes.removeIf(v -> v.getUser().equals(user));
        votes.add(new Vote(user, value));
        author.updateReputation(value * 5);  // +5 for upvote, -5 for downvote
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

    private String generateId() {
        return "QNS" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    // Getters
    public String getId() { return id; }
    public User getAuthor() { return author; }
    public String getTitle() { return title; }
    public StringBuilder getContent() { return content; }
    public LocalDate getCreationDate() { return creationDate; }
    public List<Answer> getAnswers() { return answers; }
    public List<Tag> getTags() { return tags; }
    public void updateContent(StringBuilder content) {
        this.content = content;
    }
    public void updateTags(List<String> tagNames) {
        this.tags.clear();
        tagNames.forEach(s -> this.tags.add(new Tag(s)));
    }
    public void updateAnswer(Answer answer, StringBuilder content) {
        answers.stream()
                .filter(a -> a.getId().equals(answer.getId()))
                .findFirst()
                .ifPresentOrElse(a -> a.updateContent(content),
                         () -> System.out.println("Answer not found"));
    }
    public void updateComment(Comment comment, StringBuilder content) {
        comments.stream()
                .filter(c -> c.getId().equals(comment.getId()))
                .findFirst()
                .ifPresentOrElse(c -> c.updateContent(content),
                         () -> System.out.println("Comment not found"));
    }

    public void addCommentToAnswer(Answer answer, Comment comment) {
        answers.stream()
                .filter(a -> a.getId().equals(answer.getId()))
                .findFirst()
                .ifPresentOrElse(a -> a.addComment(comment),
                         () -> System.out.println("Answer not found"));
    }
    public void voteAnswer(User voter, Answer answer, int value) {
        answers.stream()
                .filter(a -> a.getId().equals(answer.getId()))
                .forEach(a -> a.vote(voter, value));
    }
    public void acceptAnswer(Answer answer) {
        answers.stream()
                .filter(a -> a.getId().equals(answer.getId()))
                .findFirst()
                .ifPresentOrElse(Answer::markAsAccepted,
                         () -> System.out.println("Answer not found"));
    }
}