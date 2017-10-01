package VAnalysis.pageProcess;

import VAnalysis.entity.Answer;
import VAnalysis.entity.VUser;
import VAnalysis.utils.SessionFactoryUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.util.List;

import static VAnalysis.utils.Utils.saveObj;

/**
 * @author kissx on 2017/9/25.
 *         <p>
 *         抓取大 V 回答
 */
public class VAnswerPageProcess implements PageProcessor {

    private int offset = 0;
    private VUser vUser = new VUser();
    private Site site = Site
            .me()
            .setTimeOut(10000)
            .setRetrySleepTime(10)
            .setSleepTime(1000)
            .setCharset("UTF-8")
            .setDomain("www.zhihu.com")
            .addCookie("z_c0", "Mi4xZWNrSUFnQUFBQUFBa01JdklibGtEQmNBQUFCaEFsVk5Zc0RtV1FEeHQ4bGhMLU5LMW1QMlU5SUFScGhTeEZMa2R3|1505702754|f9bc058785903f93cddfd7cb9126b8b8205e605b");

    @Override
    public void process(Page page) {
        Session session = SessionFactoryUtils.getCurrentSession();
        Transaction transaction = session.getTransaction();
        if (!transaction.isActive())
            transaction = session.beginTransaction();
        String url = page.getUrl().toString();
        if (url.matches("^(https://www.zhihu.com/api/).*")) {
            Json json = page.getJson();
            List<String> answerList = json.jsonPath("$..data[*].id").all();
            List<String> questionList = json.jsonPath("$..data[*].question.id").all();
            for (int i = 0; i < answerList.size(); i++) {
                page.addTargetRequest("https://www.zhihu.com/question/" + questionList.get(i) + "/answer/" + answerList.get(i));
            }
        } else if (url.matches("^(https://www.zhihu.com/people/).*")) {
            int answerTotalNum = Integer.parseInt(page.getHtml().xpath("//span[@class=\"Tabs-meta\"]/text()").get());
            for (int i = 0; i <= answerTotalNum / 20; i++) {
                String jsonUrl = "https://www.zhihu.com/api/v4/members/" + url.substring(url.indexOf("people/") + 7, url.indexOf("/answers")) + "/answers?include=data%5B*%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Cmark_infos%2Ccreated_time%2Cupdated_time%2Creview_info%2Cquestion%2Cexcerpt%2Crelationship.is_authorized%2Cvoting%2Cis_author%2Cis_thanked%2Cis_nothelp%2Cupvoted_followees%3Bdata%5B*%5D.author.badge%5B%3F(type%3Dbest_answerer)%5D.topics&offset="
                        + offset + "&limit=20&sort_by=created&page=1";
                offset += 20;
                page.addTargetRequest(jsonUrl);
            }
            String userName = page.getHtml().xpath("//span[@class=\"ProfileHeader-name\"]/text()").get();
            vUser.setUserName(userName);
            Query query = session.createQuery("from VUser where userName = ?");
            query.setParameter(0, userName);
            List list = query.list();
            if (list == null || list.size() == 0) {
                saveObj(session, transaction, vUser);
            } else
                vUser = (VUser) list.get(0);
        } else {
            String timeStr = page.getHtml().xpath("//div[@class=\"ContentItem-time\"]/a/span/@data-tooltip").get();
            if (timeStr == null)
                timeStr = page.getHtml().xpath("//div[@class=\"ContentItem-time\"]/a/text()").get();
            timeStr = timeStr.substring(4, timeStr.length());
            Answer answer = new Answer();
            answer.setTime(timeStr);
            answer.setvUser(vUser);
            String followerStr = page.getHtml().xpath("//span[@class=\"Voters\"]/button/text()").get().split(" ")[0];
            answer.setFollower(Integer.valueOf(followerStr));
            String question = page.getHtml().xpath("//h1[@class=\"QuestionHeader-title\"]/text()").get();
            answer.setQuestion(question);
            saveObj(session, transaction, answer);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

}
