package org.bean.util;

/**
 * Created by liuyulong on 15-2-9.
 */
public class APIConstant {

    public static final int DEFAULT_PAGE_SIZE = 10;

    public static String getPageSize() {
        return DEFAULT_PAGE_SIZE + "";
    }

    public static final String APP_KEY = "80787780";

    public static final String APP_SECRET = "1bad567db80647b39cb8dceba745a7ad";

    public static final String TOKEN_URL = "https://oauth.dianping.com/token";

    public static final String AUTHORIZE_URL = "https://oauth.dianping.com/authorize";

    public static final String API_URL = "https://api.dianping.com/v1/user/get_user_info";

    public static final String DEFAULT_REDIR_URI = "https://oauth.dianping.com/index.html";

    public static final String AUTHORIZE_REQUEST_URL = AUTHORIZE_URL + "?client_id=" + APP_KEY
            + "&response_type=code&state=xyzs&scope=user_info_read&redirect_uri="
            + DEFAULT_REDIR_URI;

    public static final String FIND_BUSINESS = "http://api.dianping.com/v1/business/find_businesses";
}
