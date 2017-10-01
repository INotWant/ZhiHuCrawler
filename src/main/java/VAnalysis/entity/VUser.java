package VAnalysis.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author kissx on 2017/9/25.
 */
public class VUser {

    private Integer userId;
    private String userName;
    private String homePage;
    private Integer following;   // 关注
    private Integer followers;   // 被关注

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

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public Integer getFollowing() {
        return following;
    }
}
