package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StackOverflow {
    private static StackOverflow instance;
    private final Map<Integer, User> users;
    private final Map<String, Question> questions;
    private final Map<String, Answer> answers;
    private final List<Tag> tags;

    private StackOverflow() {
        users = new HashMap<>();
        questions = new HashMap<>();
        answers = new HashMap<>();
        tags = new ArrayList<>();
    }

    public static synchronized StackOverflow getInstance() {
        if( instance == null) {
            instance = new StackOverflow();
        }
        return instance;
    }

    public User createUser(String username, String email) {
        int id = users.size() + 1;
        User user = new User(id, username, email);
        users.put(id, user);
        return user;
    }

    public Question askQuestion(User user, String title, String content, List<String> tags) {
        Question question = user.askQuestion(title, content, tags);
        questions.put(question.getId(), question);
        for (Tag tag : question.getTags()) {
            if(!this.tags.contains(tag)) this.tags.add(tag);
        }
        return question;
    }

    public Answer answerQuestion(User user, Question question, String content) {
        Answer answer = user.answerQuestion(question, content);
        answers.put(answer.getId(), answer);
        return answer;
    }

    public Comment addComment(User user, Commentable commentable, String content) {
        return user.addComment(commentable, content);
    }

    public void voteQuestion(User user, Question question, int value) {
        question.vote(user, value);
    }

    public void voteAnswer(User user, Answer answer, int value) {
        answer.vote(user, value);
    }

    public void acceptAnswer(Answer answer) {
        answer.markAsAccepted();
    }

    public List<Question> searchQuestions(String query) {
        return questions.values().stream()
                .filter(q -> q.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        q.getContent().toLowerCase().contains(query.toLowerCase()) ||
                        q.getTags().stream().anyMatch(t -> t.getName().equalsIgnoreCase(query)))
                .collect(Collectors.toList());
    }

    public List<Question> getQuestionsByUser(User user) {
        return user.getQuestions();
    }

    // Getters
    public User getUser(int id) { return users.get(id); }
    public Question getQuestion(String id) { return questions.get(id); }
    public Answer getAnswer(String id) { return answers.get(id); }
    public Boolean getTagExists(Tag tag) { return tags.contains(tag); }
}