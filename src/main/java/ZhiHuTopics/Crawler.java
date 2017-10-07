package ZhiHuTopics;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @author kissx on 2017/9/20.
 *         <p>
 *         爬取知乎某话题下 question 的入口类
 *         <p>
 *         爬取的结果：
 *         url:	https://www.zhihu.com/question/41609070
 *         标题:	既然有http 请求，为什么还要用rpc调用？
 *         关注者:	888
 *         浏览人数:	66328
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
     * 调用此方法开始爬取 url 指定的主题。默认为开启 5 个线程、在“命令行“下输出以及保存在一个文件名为主题名的 txt 文件下。
     *
     * @param url 所要爬取的主题对应的 url （e.g. https://www.zhihu.com/topic#软件工程）
     */
    public void crawlingTopic(String url) {
        String fileName = url.substring(url.lastIndexOf("#") + 1, url.length()) + ".txt";
        crawlingTopic(url, 5, new ConsolePipeline(), new OneFilePipeline(fileName));
    }

    /**
     * 调用此方法开始爬取 url 指定的主题。
     *
     * @param url       所要爬取的主题对应的 url （e.g. https://www.zhihu.com/topic#软件工程）
     * @param threadNum 开启的线程数（建议不要开它大，否则知乎会屏蔽你的 ip）
     * @param pipelines 定制输出（详见 webMagic）
     */
    public void crawlingTopic(String url, int threadNum, Pipeline... pipelines) {
        String fileName = url.substring(url.lastIndexOf("#") + 1, url.length()) + ".txt";
        Spider spider = Spider.create(new ZhihuTopicPageProcessor(fileName, cookie))
                .addUrl(url)
                .thread(threadNum);
        for (Pipeline pipeline : pipelines) {
            spider.addPipeline(pipeline);
        }
//        SpiderManage spiderManage = SpiderManage.getInstance();
//        spiderManage.addSpider(spider);
        spider.start();
    }


    public static void main(String[] args) {
        // 案例爬取 软件工程、Java 这两个话题下的 question
        String cookie = "Mi4xZWNrSUFnQUFBQUFBWU1KM2tzeGtEQmNBQUFCaEFsVk43c2JtV1FEYS1JcFhxQklPWDZoZ2JtRmhFTTdtNGV0WDhn|1505704430|ae5934e4cddf1b5c62347e78e0cadb9cd15a4ce0";
        Crawler crawler = new Crawler(cookie);
        // 注意，这里可以多次调用 crawlingTopic() 来爬取不同话题，它们会并发地执行。但不建议开启多个，否则知乎会屏蔽你的 ip。
        crawler.crawlingTopic("https://www.zhihu.com/topic#软件工程");
        crawler.crawlingTopic("https://www.zhihu.com/topic#Java");
    }
}
