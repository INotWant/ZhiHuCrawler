package VAnalysis.entity;

/**
 * @author kissx on 2017/9/25.
 */
public class Answer {

    private Integer aId;
    private String question;
    private String answerContent;
    private String time;
    private Integer approver;
    private Integer follower;   //该问题的关注者
    private Integer browser;    //该问题的浏览者

    private VUser vUser;

    public Integer getaId() {
        return aId;
    }

    public void setaId(Integer aId) {
        this.aId = aId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public Integer getFollower() {
        return follower;
    }

    public void setFollower(Integer follower) {
        this.follower = follower;
    }

    public VUser getvUser() {
        return vUser;
    }

    public void setvUser(VUser vUser) {
        this.vUser = vUser;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public Integer getApprover() {
        return approver;
    }

    public void setApprover(Integer approver) {
        this.approver = approver;
    }

    public Integer getBrowser() {
        return browser;
    }

    public void setBrowser(Integer browser) {
        this.browser = browser;
    }
}
