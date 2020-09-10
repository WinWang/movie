package com.winwang.movie.crawler;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xuxueli.crawler.loader.PageLoader;
import com.xuxueli.crawler.loader.strategy.HtmlUnitPageLoader;
import com.xuxueli.crawler.model.PageRequest;
import com.xuxueli.crawler.util.UrlUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class MyHtmlUnitLoader extends PageLoader {
    private static Logger logger = LoggerFactory.getLogger(HtmlUnitPageLoader.class);

    public MyHtmlUnitLoader() {
    }

    public Document load(PageRequest pageRequest) {
        if (!UrlUtil.isUrl(pageRequest.getUrl())) {
            return null;
        } else {
            WebClient webClient = new WebClient();

            Document var7;
            try {
                WebRequest webRequest = new WebRequest(new URL(pageRequest.getUrl()));
                webClient.getOptions().setUseInsecureSSL(true);
                webClient.getOptions().setJavaScriptEnabled(false);
                webClient.getOptions().setCssEnabled(false);
                webClient.getOptions().setThrowExceptionOnScriptError(false);
                webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
                webClient.getOptions().setDoNotTrackEnabled(false);
                webClient.getOptions().setUseInsecureSSL(!pageRequest.isValidateTLSCertificates());
                Iterator i$;
                Map.Entry cookieItem;
                if (pageRequest.getParamMap() != null && !pageRequest.getParamMap().isEmpty()) {
                    i$ = pageRequest.getParamMap().entrySet().iterator();

                    while(i$.hasNext()) {
                        cookieItem = (Map.Entry)i$.next();
                        webRequest.getRequestParameters().add(new NameValuePair((String)cookieItem.getKey(), (String)cookieItem.getValue()));
                    }
                }

                if (pageRequest.getCookieMap() != null && !pageRequest.getCookieMap().isEmpty()) {
                    webClient.getCookieManager().setCookiesEnabled(true);
                    i$ = pageRequest.getCookieMap().entrySet().iterator();

                    while(i$.hasNext()) {
                        cookieItem = (Map.Entry)i$.next();
                        webClient.getCookieManager().addCookie(new Cookie("", (String)cookieItem.getKey(), (String)cookieItem.getValue()));
                    }
                }

                if (pageRequest.getHeaderMap() != null && !pageRequest.getHeaderMap().isEmpty()) {
                    webRequest.setAdditionalHeaders(pageRequest.getHeaderMap());
                }

                if (pageRequest.getUserAgent() != null) {
                    webRequest.setAdditionalHeader("User-Agent", pageRequest.getUserAgent());
                }

                if (pageRequest.getReferrer() != null) {
                    webRequest.setAdditionalHeader("Referer", pageRequest.getReferrer());
                }

                webClient.getOptions().setTimeout(pageRequest.getTimeoutMillis());
                webClient.setJavaScriptTimeout((long)pageRequest.getTimeoutMillis());
                webClient.waitForBackgroundJavaScript((long)pageRequest.getTimeoutMillis());
                if (pageRequest.getProxy() != null) {
                    InetSocketAddress address = (InetSocketAddress)pageRequest.getProxy().address();
                    boolean isSocks = pageRequest.getProxy().type() == Proxy.Type.SOCKS;
                    webClient.getOptions().setProxyConfig(new ProxyConfig(address.getHostName(), address.getPort(), isSocks));
                }

                if (pageRequest.isIfPost()) {
                    webRequest.setHttpMethod(HttpMethod.POST);
                } else {
                    webRequest.setHttpMethod(HttpMethod.GET);
                }

                HtmlPage page = (HtmlPage)webClient.getPage(webRequest);
                String pageAsXml = page.asXml();
                if (pageAsXml == null) {
                    return null;
                }

                Document html = Jsoup.parse(pageAsXml);
                var7 = html;
            } catch (IOException var11) {
                logger.error(var11.getMessage(), var11);
                return null;
            } finally {
                if (webClient != null) {
                    webClient.close();
                }

            }

            return var7;
        }
    }
}
