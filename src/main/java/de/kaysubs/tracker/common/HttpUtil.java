package de.kaysubs.tracker.common;

import de.kaysubs.tracker.common.exception.HttpErrorCodeException;
import de.kaysubs.tracker.common.exception.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class HttpUtil {
    public final static int TIMEOUT = 20000;

    public final static RequestConfig WITH_TIMEOUT = RequestConfig.custom()
            .setCookieSpec(CookieSpecs.STANDARD) // DEFAULT fails to parse cookies with a "expires" value
            .setConnectionRequestTimeout(TIMEOUT)
            .setConnectTimeout(TIMEOUT)
            .setSocketTimeout(TIMEOUT)
            .build();

    public static String readIntoString(HttpResponse response) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            return out.toString();
        } catch (IOException e) {
            throw new HttpException("Cannot read response content");
        }
    }

    public static void requireStatusCode(HttpResponse response, int code) {
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode != code)
            throw new HttpErrorCodeException(statusCode);
    }

    public static HttpResponse executeRequest(HttpUriRequest request) {
        return executeRequest(request, HttpClients.createDefault());
    }

    public static HttpResponse executeRequest(HttpUriRequest request, Cookie[] cookies) {
        CookieStore store = new BasicCookieStore();
        Arrays.stream(cookies).forEach(store::addCookie);
        return HttpUtil.executeRequest(request, store);
    }

    public static HttpResponse executeRequest(HttpUriRequest request, CookieStore cookieStore) {
        CloseableHttpClient client = HttpClients.custom()
                .setDefaultCookieStore(cookieStore).build();

        return executeRequest(request, client);
    }

    public static HttpResponse executeRequest(HttpUriRequest request, HttpClient client) {
        try {
            return client.execute(request);
        } catch (IOException e) {
            throw new HttpException("Cannot Execute Http request", e);
        }
    }

}
