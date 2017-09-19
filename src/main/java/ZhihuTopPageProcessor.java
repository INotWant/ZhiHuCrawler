import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author kissx on 2017/9/18.
 * 爬取知乎互联网 ----- topic
 */
public class ZhihuTopPageProcessor extends BasePageProcessImp implements PageProcessor {

    private Site site = Site.me()
            .setRetrySleepTime(10)
            .setSleepTime(2000)
            .setTimeOut(10000)
            .setCharset("UTF-8")
            .setDomain("www.zhihu.com")
            .addCookie("z_c0", "Mi4xZWNrSUFnQUFBQUFBWU1KM2tzeGtEQmNBQUFCaEFsVk43c2JtV1FEYS1JcFhxQklPWDZoZ2JtRmhFTTdtNGV0WDhn|1505704430|ae5934e4cddf1b5c62347e78e0cadb9cd15a4ce0");


    @Override
    public void process(Page page) {
        String URL_QUESTION = "https://www\\.zhihu\\.com/question/.*";
        if (page.getUrl().regex(URL_QUESTION).match()) {
            contentProcess(page);
        } else {
            addTargetRequests(page.getHtml(), page);
            startUpSpider(page.getHtml(), "//div[@class=\"feed-item feed-item-hook  folding\"]/@data-score");
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        SpiderManage spiderManage = SpiderManage.getInstance();
        Spider spider = Spider.create(new ZhihuTopPageProcessor())
                .addUrl("https://www.zhihu.com/topic#互联网")
                .addPipeline(new ConsolePipeline())
                .addPipeline(new FilePipeline("./result/"))
                .thread(5);
        spiderManage.addSpider(spider);
        spider.run();
    }
}
