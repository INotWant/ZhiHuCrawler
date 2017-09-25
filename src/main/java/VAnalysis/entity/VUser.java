package VAnalysis.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author kissx on 2017/9/25.
 */
public class VUser {

    private Integer userId;
    private String userName;

    private Set<Answer> answerSet = new HashSet<>();

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<Answer> getAnswerSet() {
        return answerSet;
    }

    public void setAnswerSet(Set<Answer> answerSet) {
        this.answerSet = answerSet;
    }
}
