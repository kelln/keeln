package com.keeln.mappertest;

import com.keeln.domain.model.ApiDataDO;
import com.keeln.manager.ApiDataManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by keeln on 17/3/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
            // System.out.println("同时 从这里也能看出 即便return了，仍然会执行finally的！");
        }
    }

    public static void main(String[] args) throws IOException, JSONException {
        JSONObject json = readJsonFromUrl("http://apis.haoservice.com/lifeservice/travel/scenery?key=4957925b82a8489c8d57a38dc205302c&pid=32&cid=&page=1&paybyvas=false");
        System.out.println(json.toString());
        JSONArray jsonArray = json.getJSONArray("result");
        jsonArray.get(0);
    }

    @Autowired
    ApiDataManager apiDataManager;
    @Test
    public void initApiDataTest() throws IOException, JSONException{
        JSONObject json = readJsonFromUrl("http://apis.haoservice.com/lifeservice/travel/scenery?key=4957925b82a8489c8d57a38dc205302c&pid=32&cid=&page=1&paybyvas=false");
        JSONArray jsonArray = json.getJSONArray("result");
        ApiDataDO apiDataDO = new ApiDataDO();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            apiDataDO.setTitle(jsonObject.getString("title"));
            apiDataDO.setGrade(jsonObject.getString("grade"));
            apiDataDO.setPriceMin(jsonObject.getString("price_min"));
            apiDataDO.setCommCnt(jsonObject.getString("comm_cnt"));
            apiDataDO.setCityid(jsonObject.getString("cityId"));
            apiDataDO.setAddress(jsonObject.getString("address"));
            apiDataDO.setSid(jsonObject.getString("sid"));
            apiDataDO.setUrl(jsonObject.getString("url"));
            apiDataDO.setImgurl(jsonObject.getString("imgurl"));
            apiDataManager.insertSelective(apiDataDO);
        }
    }
}
