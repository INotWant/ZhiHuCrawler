import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.utils.HttpConstant;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BasePageProcessImp implements BasePageProcessInterface {
    public void contentProcess(Page page) {
        page.putField("标题", page.getHtml().xpath("//h1[@class=\"QuestionHeader-title\"]/text()").get());
        List<String> list = page.getHtml().xpath("//div[@class=\"NumberBoard-value\"]/text()").all();
        if (list != null && list.size() >= 2) {
            page.putField("关注者", list.get(0));
            page.putField("浏览人数", list.get(1));
        } else
            page.setSkip(true);
    }

    public Html jsonProcess(Page page) {
        Json json = page.getJson();
        // Json 解析
        String content = json.jsonPath("$..msg").get();
        content = content.substring(2, content.length() - 2);
        // 特殊符号转换
        content = transfer(content);
        return new Html(content);
    }

    public void addTargetRequests(Html html, Page page) {
        List<String> urlList = html.xpath("//a[@class=\"question_link\"]/@href").all();
        List<String> newUrlList = new ArrayList<>();
        for (String urlStr : urlList) {
            urlStr = "https://www.zhihu.com" + urlStr;
            newUrlList.add(urlStr);
        }
        page.addTargetRequests(newUrlList);
    }

    public void startUpSpider(Html html, String xPath) {
        List<String> list = html.xpath(xPath).all();
        if (list != null && list.size() > 0) {
            String offset = list.get(list.size() - 1);
            try {
                Request request = new Request("https://www.zhihu.com/node/TopicFeedList?method=next&params=" + URLEncoder.encode("{\"offset\":" + offset + ",\"topic_id\":99,\"feed_type\":\"smart_feed\"}", "utf-8"));
                request.setMethod(HttpConstant.Method.POST);
                Spider spider = Spider.create(new ZhihuMorePageProcessor())
                        .addPipeline(new ConsolePipeline())
                        .addPipeline(new FilePipeline("./result/"))
                        .addRequest(request)
                        .thread(5);
                SpiderManage spiderManage = SpiderManage.getInstance();
                spiderManage.addSpider(spider);
                Thread.sleep(15000);
                spider.runAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private String transfer(String jsonStr) {
        jsonStr = jsonStr.replaceAll("\\\\\"", "\"");
        jsonStr = jsonStr.replaceAll("\\\\n", "<br/>");
        jsonStr = jsonStr.replaceAll("\\\\/", "/");
        return jsonStr;
    }
}
