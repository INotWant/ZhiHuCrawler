package ZhiHuTopics;

import ZhiHuTopics.PageProcessor.ZhiHuTopicPageProcessor;
import ZhiHuTopics.PipeLine.OneFilePipeline;
import ZhiHuTopics.manager.Statistics;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 爬取知乎某话题下 question 的入口类
 * <p>
 * 爬取的结果：
 * url:	https://www.zhihu.com/question/41609070
 * 标题:	既然有http 请求，为什么还要用rpc调用？
 * 关注者:	888
 * 浏览人数:	66328
 *
 * @author kissx on 2017/9/20.
 */
public class Crawler {

    private String cookie;

    /**
     * 构造 Crawler ，需要初始化 cookie 用以模拟登陆。cookie 值可以在浏览器上成功登陆知乎后获得。所需 cookie 的 key 为 z_c0。
     * 【注：需要在 https://www.zhihu.com/topic 页面下获取该 cookie】
     *
     * @param cookie cookie 值
     */
    public Crawler(String cookie) {
        this.cookie = cookie;
    }

    /**
     * 调用此方法开始爬取 url 指定的主题。默认为在“命令行“下输出以及保存在一个文件名为主题名的 txt 文件下。
     *
     * @param url 所要爬取的主题对应的 url （e.g. https://www.zhihu.com/topic#软件工程）
     */
    public void crawlingTopic(String url) {
        String fileName = url.substring(url.lastIndexOf("#") + 1, url.length()) + ".txt";
        crawlingTopic(url, new ConsolePipeline(), new OneFilePipeline(fileName));
    }

    /**
     * 调用此方法开始爬取 url 指定的主题。
     *
     * @param url       所要爬取的主题对应的 url （e.g. https://www.zhihu.com/topic#软件工程）
     * @param pipelines 定制输出（详见 webMagic）
     */
    public void crawlingTopic(String url, Pipeline... pipelines) {
        String topicName = url.substring(url.lastIndexOf("#") + 1, url.length()) + ".txt";
        Spider spider = Spider.create(new ZhiHuTopicPageProcessor(topicName, cookie, pipelines))
                .addUrl(url)
                .addPipeline(new ConsolePipeline());
        spider.start();
    }


    public static void main(String[] args) {
        // 案例爬取 软件工程、Java 这两个话题下的 question
        String cookie = "Mi4xZWNrSUFnQUFBQUFBWU1KM2tzeGtEQmNBQUFCaEFsVk43c2JtV1FEYS1JcFhxQklPWDZoZ2JtRmhFTTdtNGV0WDhn|1505704430|ae5934e4cddf1b5c62347e78e0cadb9cd15a4ce0";
        Crawler crawler = new Crawler(cookie);
        // 注意，这里可以多次调用 crawlingTopic() 来爬取不同话题，它们会并发地执行。但不建议开启多个，否则知乎会屏蔽你的 ip。
        crawler.crawlingTopic("https://www.zhihu.com/topic#软件工程");
//        crawler.crawlingTopic("https://www.zhihu.com/topic#Java");
        // 用于控制爬取的数量（获得的总 question 数目为 15 个）
//        Statistics.getInstance(15);
    }
}
