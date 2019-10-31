package com.example.demo.https;

import org.junit.Test;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @Author: zhuwei
 * @Date:2019/9/20 11:24
 * @Description: 测试https请求
 */
public class HttpsTest {

    @Test
    public void testHttps() throws IOException, NoSuchProviderException, NoSuchAlgorithmException, KeyManagementException {
        //https地址
        String httpsUrl = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=18210921867";
        //建立连接
        URL url = new URL(httpsUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        //使用自定义的信任管理器
        TrustManager[] tm = {new MyX509TrustManager()};
        SSLContext ssl = SSLContext.getInstance("SSL", "SunJSSE");
        ssl.init(null, tm, new java.security.SecureRandom());
        SSLSocketFactory ssf = ssl.getSocketFactory();
        conn.setSSLSocketFactory(ssf);
        conn.setDoInput(true);
        //设置请求方式
        conn.setRequestMethod("GET");
        //取得输入流
        InputStream inputStream = conn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        //读取响应内容
        StringBuffer buffer = new StringBuffer();
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        //关闭资源
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        conn.disconnect();
        //输出返回结果
        System.out.println(buffer);


    }
}
