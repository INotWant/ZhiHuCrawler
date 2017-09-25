package VAnalysis.pageProcess;

import VAnalysis.utils.SessionFactoryUtils;
import org.hibernate.SessionFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author kissx on 2017/9/25.
 */
public class VUserPageProcess implements PageProcessor {

    private static SessionFactory sessionFactory = SessionFactoryUtils.getSessionFactory();

    private Site site = Site
            .me()
            .setTimeOut(10000)
            .setRetrySleepTime(10)
            .setSleepTime(1000)
            .setCharset("UTF-8")
            .setDomain("www.zhihu.com")
            .addCookie("z_c0", "Mi4xZWNrSUFnQUFBQUFBWU1KM2tzeGtEQmNBQUFCaEFsVk43c2JtV1FEYS1JcFhxQklPWDZoZ2JtRmhFTTdtNGV0WDhn|1505704430|ae5934e4cddf1b5c62347e78e0cadb9cd15a4ce0");

    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

    }
}
