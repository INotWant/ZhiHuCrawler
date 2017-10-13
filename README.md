# ZhiHuCrawler（基于 `webmagic` 的知乎爬取）

## 简介

出于兴趣想要分析一下`知乎`，所以爬取了一些知乎的数据。爬取的数据主要有三种：
- **某种话题（如互联网、软件工程）下的问题**
- **知乎大`V`（如张佳玮、李开复等）**
- **大`V`回答**

## 模块

主要分两个模块：

1. `话题问题爬取`（`ZhiHuTopics` package）

> 该模块主要爬取某话题下的 `Question` ，比如爬取 `软件工程` 下的问题。爬取的结果如下：

    url:	https://www.zhihu.com/question/66519221
    标题:	腾讯开发微信花了多少钱？真的技术难度这么大吗？难点在哪里？
    关注者:	2955
    浏览人数:	1288594

>> 【**注**】：这里由于我不需要 `问题回答` ，故没有爬取 `用户回答` 。其实，在此基础上修改一下很容易得到 `用户回答` 内容。

> 该模块下爬取内容的输出为 `txt` 文件。

> 该模块的使用案例，请参考 `Crawler` 类中 `main()` 方法。

2. `大v爬取` (`VAnalysis` package)【未添加注释。。。】

> 该模块爬取的数据可分为两类：

>> **知乎大`v`** 【案例】: `VUserCrawler` 类中 `main()`

>>> ![users](/src/main/resources/img/users.jpg)

>> **大`V`回答** 【案例】: `VAnswerCrawler` 类中 `main()`

>>> ![answers](/src/main/resources/img/answers.jpg)

> 该模块爬取的结果输出到 `MySQL` 数据库。其中，使用了 `Hibernate` 方便、优化了输出。

## 相关

- 如果需要修改，请先了解 `WebMagic`。[**WebMagic**](https://github.com/code4craft/webmagic) 是一个开源的Java垂直爬虫框架。

- 为防止知乎锁 `IP` ，爬取速度不是很快。我在爬 `top100大v的回答`（8w+数据） 时大约使用了 **2天 17小时**。

## 关于

    IntelliJ IDEA 2017.1
    Build #IU-171.3780.107, built on March 22, 2017
    Licensed to kissx
    
    JRE: 1.8.0_112-release-736-b13 amd64
    JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
    Windows 10 10.0