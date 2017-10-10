package VAnalysis.crawler;

import VAnalysis.pageProcess.VUserPageProcess;
import ZhiHuTopics.manager.SpiderManage;
import ZhiHuTopics.manager.Statistics;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.PriorityScheduler;

/**
 * @author kissx on 2017/10/9.
 */
public class VUserCrawler {

    private String cookie;

    public VUserCrawler(String cookie, Integer num) {
        this.cookie = cookie;
        if (num != null)
            Statistics.getInstance(num);
    }

    public void crawlingVUser(String... urls) {
        crawlingVUser(50000, urls);
    }

    public void crawlingVUser(int fCount, String... urls) {
        Spider spider = Spider.create(new VUserPageProcess(fCount, cookie))
                .addUrl(urls)
                .addPipeline(new ConsolePipeline())
                .setScheduler(new PriorityScheduler());
        SpiderManage.getInstance().addSpider(spider);
        spider.start();
    }

    public static void main(String[] args) {
        String cookie = "Mi4xZWNrSUFnQUFBQUFBWU1KM2tzeGtEQmNBQUFCaEFsVk43c2JtV1FEYS1JcFhxQklPWDZoZ2JtRmhFTTdtNGV0WDhn|1505704430|ae5934e4cddf1b5c62347e78e0cadb9cd15a4ce0";
        VUserCrawler vUserCrawler = new VUserCrawler(cookie, 10);
        vUserCrawler.crawlingVUser("https://www.zhihu.com/people/zhang-jia-wei/following");
    }
}
