package gatogoku.epsonapagar;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Hashtable;
public class WebRequest {
    private String userAgent;
    private String responseString;
    private int responseCode;
    private String exceptionMessage;
    private Hashtable<String,String> cookies;
    public WebRequest() {
        this.userAgent = "EvilBlackDeathOfDoom browser v1.0";
        cookies = new Hashtable<String,String>();
    }
    public boolean get (String urlString) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.setConnectTimeout(0);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Basic " + Base64.encode("EPSONWEB:admin".getBytes(), Base64.NO_WRAP));
            conn.setRequestProperty("Accept","*/*");
            conn.setRequestProperty("Accept-Encoding","gzip, deflate");
            conn.setRequestProperty("Accept-Language","en-US,en;q=0.9,es;q=0.8");
            conn.setRequestProperty("Connection","keep-alive");
            conn.setRequestProperty("Referer","http://192.168.1.140/cgi-bin/webremote");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
            conn.setRequestProperty("X-Requested-With","XMLHttpRequest");
            conn.setRequestProperty("content-type","text/html");
            conn.setUseCaches(false);
            conn.connect();
            int Resp = conn.getResponseCode();
            String c = conn.getResponseMessage();
        } catch (Exception e) {
            //throw new RuntimeException(e);
        }
        return false;
    }

    private void setCookies(HttpURLConnection connection) {
        String cookieString = "";

        // Get cookies name=value pair from hashtable
        for (String cookieName : cookies.keySet()) {
            cookieString += cookieName + "=" + cookies.get(cookieName) + ";";
        }

        // and put them in the request header
        System.out.println("Sending cookies to server: " + cookieString);
        connection.setRequestProperty("Cookies", cookieString);
    }

    /**
     * retrieves Cookies sent by server
     * Cookies come in this form:
     *  Set-Cookie: name1=value1;
     *  Set-Cookie: name2=value2;
     *  ...
     * So we have to retrieve every Set-Cookie line and parse cookie name and value
     * This method stores cookie data in the cookies Hashtable for further use
     * @param connection
     */
    private void getCookies(HttpURLConnection connection) {
        String headerName=null;
        String cookieString = "";
        String cookieName = "";
        String cookieValue = "";

        // We look up for Set-Cookie entries in header
        for (int i=1; (headerName = connection.getHeaderFieldKey(i))!=null; i++) {
            if (headerName.equals("Set-Cookie")) {
                cookieString = connection.getHeaderField(i);
                cookieString = cookieString.substring(0, cookieString.indexOf(";"));
                cookieName = cookieString.substring(0, cookieString.indexOf("="));
                cookieValue = cookieString.substring(cookieString.indexOf("=") + 1, cookieString.length());
                cookies.put(cookieName, cookieValue);
                System.out.println("One cookie, mmm yummy: " + cookieName + "=" + cookieValue);
            }
        }
    }

    /**
     * @return the userAgent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * @param userAgent the userAgent to set
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * @return the responseString
     */
    public String getResponseString() {
        return responseString;
    }

    /**
     * @param responseString the responseString to set
     */
    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    /**
     * @return the responseCode
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * @param responseCode the responseCode to set
     */
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * @return the exceptionMessage
     */
    public String getExceptionMessage() {
        return exceptionMessage;
    }

    /**
     * @param exceptionMessage the exceptionMessage to set
     */
    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
