package websiteschema.mpsegment.web.util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RequestHelper {

    HttpServletRequest request;

    public RequestHelper(HttpServletRequest request) {
        this.request = request;
    }

    public Map<String, String> getParams() throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<String, String>();
        String queryString = request.getQueryString();
        if (null != queryString) {
            queryString = java.net.URLDecoder.decode(queryString, "UTF-8");
            String[] strings = queryString.split("&");
            for (String str : strings) {
                String[] keyValuePair = str.split("=");
                if (keyValuePair.length == 2) {
                    params.put(keyValuePair[0], keyValuePair[1]);
                }
            }
        }
        return params;
    }

}
