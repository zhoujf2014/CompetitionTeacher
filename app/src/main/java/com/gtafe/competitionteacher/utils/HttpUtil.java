package com.gtafe.competitionteacher.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;


public class HttpUtil
{
	
	private final static int DEFAULT_CONNECT_TIMEOUT = 5000;
	private final static int DEFAULT_READ_TIMEOUT = 5000;

    /**
     * @param url url地址
     * @param params 参数
     * @param sessionid sessionid
     * @return String 返回字符�?
     * @throws Exception 异常
     */
    public static String request(String url, Map<String, String> params, String sessionid) throws Exception
    {
    	return request(url, params, null, null, sessionid);

    }
    
    

    /**
     * @param url
     * @param params
     * @param connectTimeout
     * @param readTimeout
     * @param sessionid
     * @return
     * @throws Exception
     */
    public static String request(String url, Map<String, String> params, Integer connectTimeout, Integer readTimeout, String sessionid) throws Exception
    {
        URL u = null;
        HttpURLConnection con = null;
        // 构建请求参数
        StringBuffer sb = new StringBuffer();
        if (params != null)
        {
            for (Entry<String, String> e : params.entrySet())
            {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
        }
        System.out.println("send_url:" + url);
        System.out.println("send_data:" + sb.toString());
        
        connectTimeout = connectTimeout == null ? DEFAULT_CONNECT_TIMEOUT : connectTimeout;
        readTimeout = readTimeout == null ? DEFAULT_READ_TIMEOUT : readTimeout;
        
        // 尝试发�?请求
        try
        {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            
            //2015-10-26 yuanjz 改为token信息
            if (null != sessionid && !"".equals(sessionid.trim())) {
            	con.setRequestProperty("Cookie", String.format("JSESSIONID=%s",sessionid));
            }
           
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(connectTimeout);
            con.setReadTimeout(readTimeout);
            con.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            OutputStreamWriter osw = new OutputStreamWriter(
                    con.getOutputStream(), "UTF-8");
            osw.write(sb.toString());
            osw.flush();
            osw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
     /*   finally
        {
            if (con != null)
            {
                con.disconnect();
            }
        }*/
        Thread.sleep(2000);
        // 读取返回内容
        StringBuffer buffer = new StringBuffer();
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null)
            {
                buffer.append(temp);
                buffer.append("\n");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return buffer.toString();

    }
    
    /**
     * �?��ip端口是否能够连接，如果port <=0 则只�?��ip�?
     * @param ip
     * @param port
     * @return
     */


    
}
