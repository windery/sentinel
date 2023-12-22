package cn.windery.sentinel.sentinel.adapter;

import cn.windery.sentinel.sentinel.util.UrlUtil;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

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
