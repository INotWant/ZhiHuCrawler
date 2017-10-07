package ZhiHuTopics.manager;

import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link SpiderManage} 类管理 Spider 对象，结合 {@link Statistics} 类用于在爬取到足够多的 question 后关闭 spider。
 * 此类被设计为单例模式。
 *
 * @author kissx
 */
public class SpiderManage {

    private static SpiderManage spiderManage = new SpiderManage();
    private List<Spider> spiders = new ArrayList<>();

    private SpiderManage() {
    }

    /**
     * 获取该类对象。
     */
    public static SpiderManage getInstance() {
        return spiderManage;
    }

    /**
     * 利用集合保存 Spider 对象，方便所需时统一关闭。
     *
     * @param spider Spider 对象
     */
    public synchronized void addSpider(Spider spider) {
        spiders.add(spider);
    }

    /**
     * 关闭所有 spider
     */
    public synchronized void deleteAll() {
        if (spiders.size() > 0) {
            for (Spider spider : spiders) {
                if (spider != null) {
                    spider.close();
                }
            }
        }
    }


}
