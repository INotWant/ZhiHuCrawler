package VAnalysis.pageProcess;

import VAnalysis.entity.Answer;
import VAnalysis.entity.VUser;
import VAnalysis.utils.SessionFactoryUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.util.List;

/**
 * @author kissx on 2017/9/25.
 */
public class VUserPageProcess implements PageProcessor {

    private VUser vUser = new VUser();

    private static SessionFactory sessionFactory = SessionFactoryUtils.getSessionFactory();
    private static Session session = SessionFactoryUtils.getCurrentSession();

    private Site site = Site
            .me()
            .setTimeOut(10000)
            .setRetrySleepTime(10)
            .setSleepTime(2000)
            .setCharset("UTF-8")
            .setDomain("www.zhihu.com")
            .addCookie("z_c0", "Mi4xZWNrSUFnQUFBQUFBWU1KM2tzeGtEQmNBQUFCaEFsVk43c2JtV1FEYS1JcFhxQklPWDZoZ2JtRmhFTTdtNGV0WDhn|1505704430|ae5934e4cddf1b5c62347e78e0cadb9cd15a4ce0");

    @Override
    public void process(Page page) {
        String url = page.getUrl().toString();
        if (url.matches("^(https://www.zhihu.com/api/).*")) {
            Json json = page.getJson();
            List<String> answerList = json.jsonPath("$..data[*].id").all();
            List<String> questionList = json.jsonPath("$..data[*].question.id").all();
            System.out.println(answerList);
            for (int i = 0; i < answerList.size(); i++) {
                page.addTargetRequest("https://www.zhihu.com/question/" + questionList.get(i) + "/answer/" + answerList.get(i));
            }
        } else if (url.matches("^(https://www.zhihu.com/people/).*")) {
            List<String> urlList = page.getHtml().links().regex("https://www.zhihu.com/question/[\\d+]+/answer/[\\d+]+").all();
            page.addTargetRequests(urlList);
            String jsonUrl = url.substring(0, url.indexOf("/answers")) + "/answers?include=data%5B*%5D.is_normal%2Cadmin_closed_comment%2Creward_info%2Cis_collapsed%2Cannotation_action%2Cannotation_detail%2Ccollapse_reason%2Ccollapsed_by%2Csuggest_edit%2Ccomment_count%2Ccan_comment%2Ccontent%2Cvoteup_count%2Creshipment_settings%2Ccomment_permission%2Cmark_infos%2Ccreated_time%2Cupdated_time%2Creview_info%2Cquestion%2Cexcerpt%2Crelationship.is_authorized%2Cvoting%2Cis_author%2Cis_thanked%2Cis_nothelp%2Cupvoted_followees%3Bdata%5B*%5D.author.badge%5B%3F(type%3Dbest_answerer)%5D.topics&offset=5&limit=15&sort_by=created&page=1";
            page.addTargetRequest(jsonUrl);
            if (url.charAt(url.length() - 2) == '=' && url.charAt(url.length() - 1) == '1') {
                List<String> pageList = page.getHtml().xpath("//button[@class=\"Button PaginationButton Button--plain\"]/text()").all();
                String urlPrefix = url.substring(0, url.lastIndexOf('=') + 1);
                for (int i = 2; i < Integer.parseInt(pageList.get(pageList.size() - 1)); i++) {
                    page.addTargetRequest(urlPrefix + i);
                    page.addTargetRequest(jsonUrl.substring(0, jsonUrl.lastIndexOf("=") + 1) + i);
                }
            } else {
                String userName = page.getHtml().xpath("//span[@class=\"ProfileHeader-name\"]/text()").get();
                vUser.setUserName(userName);
                session.save(vUser);
            }
        } else {
            String timeStr = page.getHtml().xpath("//div[@class=\"ContentItem-time\"]/a/span/@data-tooltip").get();
            timeStr = timeStr.substring(4,timeStr.length());
            Answer answer = new Answer();
            answer.setTime(timeStr);
            answer.setvUser(vUser);
//            answer.setFollower();
//            answer.setQuestion();
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void destroy() {
        if (sessionFactory != null)
            sessionFactory.close();
    }

    public static void main(String[] args) {
        Spider spider = Spider.create(new VUserPageProcess())
                .addUrl("https://www.zhihu.com/people/kaifulee/answers?page=1")
                .addPipeline(new ConsolePipeline());
        spider.run();
        destroy();
    }
}
