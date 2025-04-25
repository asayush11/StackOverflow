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
    private static final int QUESTION_REPUTATION = 5;
    private static final int ANSWER_REPUTATION = 10;
    private static final int COMMENT_REPUTATION = 2;

    private StackOverflow() {
        users = new HashMap<>();
        questions = new HashMap<>();
    }

    public static synchronized StackOverflow getInstance() {
        if( instance == null) {
            synchronized (StackOverflow.class) {
                if (instance == null) {
                    instance = new StackOverflow();
                }
            }
        }
        return instance;
    }

    public User createUser(String username, String email) {
        int id = users.size() + 1;
        User user = new User(id, username, email);
        users.put(id, user);
        return user;
    }

    public Question askQuestion(User user, String title, StringBuilder content, List<String> tags) {
        Question question = new Question(user, title, content, tags);
        questions.put(question.getId(), question);
        user.updateReputation(QUESTION_REPUTATION); // +5 reputation for asking a question
        return question;
    }

    public Answer answerQuestion(User user, Question question, StringBuilder content) {
        Answer answer = new Answer(user, content);
        questions.get(question.getId()).addAnswer(answer);
        user.updateReputation(ANSWER_REPUTATION); // +10 reputation for answering a question
        return answer;
    }

    public Comment addCommentOnQuestion(User user, Question question, StringBuilder content) {
        if (question.getAuthor().equals(user)) {
            System.out.println("You cannot comment on your own question");
            return null;
        }
        Comment comment = new Comment(user, content);
        questions.get(question.getId()).addComment(comment);
        user.updateReputation(COMMENT_REPUTATION); // +2 reputation for commenting
        return comment;
    }

    public Comment addCommentOnAnswer(User user, Question question, Answer answer, StringBuilder content) {
        if (question.getAuthor().equals(user)) {
            System.out.println("You cannot comment on your own question");
            return null;
        }
        Comment comment = new Comment(user, content);
        questions.get(question.getId()).addCommentToAnswer(answer,comment);
        user.updateReputation(COMMENT_REPUTATION); // +2 reputation for commenting
        return comment;
    }

    public void voteQuestion(User user, Question question, int value) {
        questions.get(question.getId()).vote(user, value);
    }

    public void voteAnswer(User user, Question question, Answer answer, int value) {
        questions.get(question.getId()).voteAnswer(user, answer, value);
    }

    public void acceptAnswer(Question question, Answer answer) {
        questions.get(question.getId()).acceptAnswer(answer);
    }

    public List<Question> searchQuestions(String query) {
        return questions.values().stream()
                .filter(q -> q.getTitle().equalsIgnoreCase(query) ||
                        q.getContent().toString().toLowerCase().contains(query.toLowerCase()) ||
                        q.getTags().stream().anyMatch(t -> t.name().equalsIgnoreCase(query)))
                .collect(Collectors.toList());
    }

    public List<Question> getQuestionsByUser(User user) {
        List<Question> userQuestions = new ArrayList<>();
        userQuestions = questions.values().stream()
                .filter(q -> q.getAuthor().equals(user))
                .collect(Collectors.toList());
        return userQuestions;
    }

    // Getters
    public User getUser(int id) { return users.get(id); }
    public Question getQuestion(String id) { return questions.get(id); }

}