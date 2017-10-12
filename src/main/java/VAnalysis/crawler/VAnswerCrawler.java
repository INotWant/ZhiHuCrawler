package VAnalysis.crawler;

import VAnalysis.entity.VUser;
import VAnalysis.pageProcess.VAnswerPageProcess;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.PriorityScheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kissx on 2017/10/9.
 */
public class VAnswerCrawler {

    private String cookie;
    private List<String> answerUrlList = new ArrayList<>();

    public VAnswerCrawler(String cookie, int start, int end) throws Exception {
        this.cookie = cookie;
        List userURLList = getUserURLs();
        if (userURLList == null)
            throw new Exception("No User!!!");
        for (int i = 0; i < userURLList.size(); i++) {
            if (start >= i + 1 && end <= i + 1) {
                VUser user = (VUser) userURLList.get(i);
                answerUrlList.add(user.getHomePage() + "/answers");
            } else if (end < i + 1)
                break;
        }
    }

    public VAnswerCrawler(String cookie) {
        this.cookie = cookie;
    }

    private List getUserURLs() {
        SessionFactory sf = new Configuration().configure().buildSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        try {
            Query query = session.createQuery("from VUser");
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
            sf.close();
        }
        return null;
    }

    public void crawlingAnswer(String... answerUrls) {
        Spider spider = Spider.create(new VAnswerPageProcess(cookie))
                .addUrl(answerUrls)
                .addPipeline(new ConsolePipeline())
                .setScheduler(new PriorityScheduler());
        spider.start();
    }

    public void crawlingAnswer() {
        crawlingAnswer(answerUrlList.toArray(new String[answerUrlList.size()]));
    }

    public static void main(String[] args) throws Exception {
        String cookie = "Mi4xZWNrSUFnQUFBQUFBWU1KM2tzeGtEQmNBQUFCaEFsVk43c2JtV1FEYS1JcFhxQklPWDZoZ2JtRmhFTTdtNGV0WDhn|1505704430|ae5934e4cddf1b5c62347e78e0cadb9cd15a4ce0";
        VAnswerCrawler vAnswerCrawler = new VAnswerCrawler(cookie, 2, 2);
        vAnswerCrawler.crawlingAnswer();
    }

}
