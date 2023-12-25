package cn.windery.sentinel.adapter;

import cn.windery.sentinel.util.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlResourceAdapter implements ResourceAdapter<String> {

    private static final Logger log = LoggerFactory.getLogger(UrlResourceAdapter.class);

    public String getResource(String url) {

        try {
            String domain = UrlUtil.extractDomain(url);
            return domain;
        } catch (Exception e) {

            return null;
        }
    }

}
