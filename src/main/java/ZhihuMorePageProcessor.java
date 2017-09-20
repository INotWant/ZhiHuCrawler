import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * @author kissx on 2017/9/18.
 * 爬取知乎互联网 ----- topic
 */
public class ZhihuMorePageProcessor extends BasePageProcessImp implements PageProcessor {

    private String fileName;
    private String topic_id;

    public ZhihuMorePageProcessor(String fileName,String topic_id) {
        this.fileName = fileName;
        this.topic_id = topic_id;
    }

    private Site site = Site.me()
            .setRetrySleepTime(10)
            .setSleepTime(1000)
            .setTimeOut(10000)
            .setCharset("UTF-8");

    @Override
    public void process(Page page) {
        String URL_QUESTION = "https://www\\.zhihu\\.com/question/.*";
        if (page.getUrl().regex(URL_QUESTION).match()) {
            contentProcess(page);
        } else {
            Html html = jsonProcess(page);
            addTargetRequests(html, page);
            startUpSpider(html,fileName,topic_id);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
