package ZhiHuTopics;

import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.List;

public class SpiderManage {

    private static SpiderManage spiderManage = new SpiderManage();
    private List<Spider> spiders = new ArrayList<>();

    private SpiderManage(){}

    public static SpiderManage getInstance(){
        return spiderManage;
    }

    public synchronized void addSpider(Spider spider) {
        spiders.add(spider);
    }

    public synchronized void deleteAll() {
        if (spiders.size() > 0) {
            for (Spider spider : spiders) {
                if (spider != null) {
                    spider.stop();
                }
            }
        }
    }



}
