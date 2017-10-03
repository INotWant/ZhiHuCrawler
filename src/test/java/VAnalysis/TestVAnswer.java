package VAnalysis;

import VAnalysis.entity.VUser;
import VAnalysis.pageProcess.VAnswerPageProcess;
import VAnalysis.utils.SessionFactoryUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
        Session session = SessionFactoryUtils.getCurrentSession();
        session.beginTransaction();

        Query query = session.createQuery("from VUser order by followers desc");
        List resultList = query.list();
        if (resultList != null){
            int count = 0;
            for(Object obj : resultList){
                VUser user = (VUser) obj;
                userURLList.add(user.getHomePage() + "/answers");
                if (++count == num)
                    break;
            }
        }
        return userURLList;
    }

   @Test
    public void test() {
        List<String> userURL = getUserURL(100);
        String[] urls = userURL.toArray(new String[userURL.size()]);
        Spider spider = Spider.create(new VAnswerPageProcess())
                .addUrl(urls)
                .addPipeline(new ConsolePipeline());
        spider.run();
    }

}
