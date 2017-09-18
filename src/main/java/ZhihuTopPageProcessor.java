import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.HttpConstant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kissx on 2017/9/18.
 *         爬取知乎互联网 ----- topic
 */
public class ZhihuTopPageProcessor implements PageProcessor {

    private Site site = Site.me()
            .setRetrySleepTime(3)
            .setSleepTime(2000)
            .setTimeOut(10000)
            .setCharset("UTF-8")
            .setDomain("www.zhihu.com")
            .addCookie("z_c0", "Mi4xZWNrSUFnQUFBQUFBWU1KM2tzeGtEQmNBQUFCaEFsVk43c2JtV1FEYS1JcFhxQklPWDZoZ2JtRmhFTTdtNGV0WDhn|1505704430|ae5934e4cddf1b5c62347e78e0cadb9cd15a4ce0");


    @Override
    public void process(Page page) {
        String URL_QUESTION = "https://www\\.zhihu\\.com/question/.*";
        if (page.getUrl().regex(URL_QUESTION).match()) {
            page.putField("标题", page.getHtml().xpath("//h1[@class=\"QuestionHeader-title\"]/text()").get());
            List<String> list = page.getHtml().xpath("//div[@class=\"NumberBoard-value\"]/text()").all();
            if (list != null && list.size() >= 2) {
                page.putField("关注者", list.get(0));
                page.putField("浏览人数", list.get(1));
            } else
                page.setSkip(true);
        } else {
            List<String> urlList = page.getHtml().xpath("//a[@class=\"question_link\"]/@href").all();
            List<String> newUrlList = new ArrayList<>();
            for (String urlStr : urlList) {
                urlStr = "https://www.zhihu.com" + urlStr;
                newUrlList.add(urlStr);
            }
            page.addTargetRequests(newUrlList);
            String xsrf = page.getHtml().xpath("//input[@name=\"_xsrf\"]/@value").get();
            List<String> list = page.getHtml().xpath("//div[@class=\"feed-item feed-item-hook  folding\"]/@data-score").all();
            if (list != null && list.size() > 0 && xsrf != null) {
                String offset = list.get(list.size() - 1);
                try {
                    Request request = new Request("https://www.zhihu.com/node/TopicFeedList?method=next&params=" + URLEncoder.encode("{\"offset\":" + offset + ",\"topic_id\":99,\"feed_type\":\"smart_feed\"}", "utf-8"));
                    request.setMethod(HttpConstant.Method.POST);
                    Spider.create(new ZhihuMorePageProcessor())
                            .addPipeline(new ConsolePipeline())
                            .addRequest(request)
                            .thread(5)
                            .run();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider spider = Spider.create(new ZhihuTopPageProcessor())
                .addUrl("https://www.zhihu.com/topic#互联网")
                .addPipeline(new FilePipeline("./result"))
                .addPipeline(new ConsolePipeline())
                .thread(5);
        spider.run();
    }
}
