package VAnalysis.pageProcess;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author kissx on 2017/10/1.
 *         <p>
 *         抓取大 V
 */
public class VUserPageProcess implements PageProcessor {

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

    }

    @Override
    public Site getSite() {
        return site;
    }
}
