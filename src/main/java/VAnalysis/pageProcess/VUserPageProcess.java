package VAnalysis.pageProcess;

import VAnalysis.entity.VUser;
import VAnalysis.utils.SessionFactoryUtils;
import VAnalysis.utils.Utils;
import ZhiHuTopics.manager.SpiderManage;
import ZhiHuTopics.manager.Statistics;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.util.List;

/**
 * @author kissx on 2017/10/1.
 *         <p>
 *         抓取大 V
 */
public class VUserPageProcess implements PageProcessor {

    private final String HEAD = "https://www.zhihu.com/api/v4/members/";
    private final String MID = "/followees?include=data%5B*%5D.answer_count%2Carticles_count%2Cgender%2Cfollower_count%2Cis_followed%2Cis_following%2Cbadge%5B%3F(type%3Dbest_answerer)%5D.topics&offset=";
    private final String TAIL = "&limit=20";

    private int fCount = 50000;       // 关注人数
    private String cookie;

    public VUserPageProcess(int fCount, String cookie) {
        this.fCount = fCount;
        this.cookie = cookie;
    }

    public VUserPageProcess(String cookie) {
        this.cookie = cookie;
    }

    private Site site = Site
            .me()
            .setTimeOut(10000)
            .setRetrySleepTime(10)
            .setSleepTime(2000)
            .setCharset("UTF-8")
            .setDomain("www.zhihu.com")
            .addCookie("z_c0", cookie);

    @Override
    public void process(Page page) {
        Statistics statistics = Statistics.getInstance();
        String url = page.getUrl().toString();
        if (statistics == null || statistics.compareDemandNum()) {
            if (url.matches("https://www.zhihu.com/people/.*")) {
                String userToken = url.substring(url.indexOf("people/") + 7, url.indexOf("/following"));
                List<String> valueList = page.getHtml().xpath("//div[@class=\"NumberBoard-value\"]/text()").all();
                if (valueList != null && valueList.size() >= 2) {
                    int following = Integer.parseInt(valueList.get(0));
                    int followers = Integer.parseInt(valueList.get(1));
                    String userName = page.getHtml().xpath("//span[@class=\"ProfileHeader-name\"]/text()").get();
                    String homePage = url.substring(0, url.lastIndexOf("/following"));
                    VUser vUser = new VUser();
                    vUser.setUserName(userName);
                    vUser.setFollowers(followers);
                    vUser.setFollowing(following);
                    vUser.setHomePage(homePage);
                    Session currentSession = SessionFactoryUtils.getCurrentSession();
                    Transaction transaction = currentSession.getTransaction();
                    if (!transaction.isActive())
                        transaction = currentSession.beginTransaction();
                    Query query = currentSession.createQuery("from VUser where userName = ?");
                    query.setParameter(0, userName);
                    List list = query.list();
                    if (list == null || list.size() == 0) {
                        Utils.saveObj(currentSession, transaction, vUser);
                        if (statistics != null)
                            statistics.increase();
                    }
                    // 获取关注列表
                    int offset = 0;
                    for (int i = 0; i <= following / 20; i++) {
                        String newURL = HEAD + userToken + MID + offset + TAIL;
                        offset += 20;
                        page.addTargetRequest(newURL);
                    }
                } else
                    page.setSkip(true);
            } else {
                Json json = page.getJson();
                List<String> urlTokenList = json.jsonPath("$..data[*].url_token").all();
                List<String> followerCountList = json.jsonPath("$..data[*].follower_count").all();
                for (int i = 0; i < followerCountList.size(); i++) {
                    if (Integer.parseInt(followerCountList.get(i)) >= fCount) {
                        String newURL = "https://www.zhihu.com/people/" + urlTokenList.get(i) + "/following";
                        // 提高此类请求的优先级，为了尽快获取用户
                        page.addTargetRequest(new Request(newURL).setPriority(Long.MAX_VALUE));
                    }
                }
            }
        } else
            SpiderManage.getInstance().deleteAll();
    }

    @Override
    public Site getSite() {
        return site;
    }
}
