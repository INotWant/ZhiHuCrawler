import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

/**
 * @author kissx on 2017/9/20.
 */
public class Crawler {

    private String cookie;

    public Crawler(String cookie) {
        this.cookie = cookie;
    }

    public void crawlingTopic(String url) {
        String fileName = url.substring(url.lastIndexOf("#") + 1, url.length()) + ".txt";
        Spider spider = Spider.create(new ZhihuTopPageProcessor(fileName,cookie))
                .addUrl(url)
                .addPipeline(new ConsolePipeline())
                .addPipeline(new OneFilePipeline(fileName))
                .addRequest()
                .thread(7);
        SpiderManage spiderManage = SpiderManage.getInstance();
        spiderManage.addSpider(spider);
        spider.run();
    }

    public static void main(String[] args) {
        String cookie = "Mi4xZWNrSUFnQUFBQUFBWU1KM2tzeGtEQmNBQUFCaEFsVk43c2JtV1FEYS1JcFhxQklPWDZoZ2JtRmhFTTdtNGV0WDhn|1505704430|ae5934e4cddf1b5c62347e78e0cadb9cd15a4ce0";
        Crawler crawler = new Crawler(cookie);
        crawler.crawlingTopic("https://www.zhihu.com/topic#软件工程");
//        crawler.crawlingTopic("https://www.zhihu.com/topic#Java");
    }

}
