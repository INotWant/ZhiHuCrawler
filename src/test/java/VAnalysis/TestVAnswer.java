package VAnalysis;

import VAnalysis.entity.VUser;
import VAnalysis.pageProcess.VAnswerPageProcess;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.Test;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kissx on 2017/10/1.
 */
public class TestVAnswer {

    private static List<String> getUserURL(int num) {
        List<String> userURLList = new ArrayList<>();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            transaction.begin();
            Query query = session.createQuery("from VUser order by followers desc");
            List resultList = query.list();
            if (resultList != null) {
                int count = 0;
                for (Object obj : resultList) {
                    VUser user = (VUser) obj;
                    userURLList.add(user.getHomePage() + "/answers");
                    if (++count == num)
                        break;
                }
            }
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            session.close();
            sessionFactory.close();
        }
        return userURLList;
    }

    @Test
    public void test() {
        List<String> userURL = getUserURL(100);
        String[] urls = userURL.toArray(new String[userURL.size()]);
        Spider spider = Spider.create(new VAnswerPageProcess("Mi4xZWNrSUFnQUFBQUFBWU1KM2tzeGtEQmNBQUFCaEFsVk43c2JtV1FEYS1JcFhxQklPWDZoZ2JtRmhFTTdtNGV0WDhn|1505704430|ae5934e4cddf1b5c62347e78e0cadb9cd15a4ce0"))
                .addUrl(urls)
                .addPipeline(new ConsolePipeline());
        spider.run();
    }

}
