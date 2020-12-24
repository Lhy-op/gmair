//package finley.gmair.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import finley.gmair.dao.CityAirQualityDao;
//import finley.gmair.model.air.CityAirQuality;
//import finley.gmair.service.AirQualityCacheService;
//import finley.gmair.service.CityAQIService;
//import finley.gmair.util.ResponseCode;
//import finley.gmair.util.ResultData;
//import org.apache.commons.codec.binary.Base64;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeDriverService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.awt.*;
//import java.awt.event.KeyEvent;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@PropertySource("classpath:/airquality.properties")
//public class CityAQIServiceImpl implements CityAQIService {
//
//    private Logger logger = LoggerFactory.getLogger(CityAQIServiceImpl.class);
//
//    @Value("${account}")
//    private String account;
//
//    @Value("${password}")
//    private String password;
//
//    @Value("${retry_count}")
//    private int count;
//
//    @Value("${driver_path}")
//    private String path;
//
//    private Robot robot;
//
//    @Autowired
//    private AirQualityCacheService airQualityCacheService;
//
//    @Autowired
//    private CityAirQualityDao cityAirQualityDao;
//
//    @PostConstruct
//    public void init() {
//        System.setProperty("webdriver.chrome.driver", path);
//        System.setProperty("java.awt.headless", "false");
//        try {
//            robot = new Robot();
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//    }
//
//    @Override
//    public ResultData obtain() {
//        ResultData result = new ResultData();
//        StringBuffer url = new StringBuffer("http://datacenter.mep.gov.cn/websjzx/api/api/air/getAirHours.vm?");
////        ProfilesIni ini = new ProfilesIni();
////        FirefoxProfile profile = ini.getProfile("default");
//        int cd = count;
//        String content = "[]";
//        while (cd > 0) {
//            try {
//
//                URL location = new URL(url.toString());
//                HttpURLConnection connection = (HttpURLConnection) location.openConnection();
//                String origin = new StringBuffer(account).append(":").append(password).toString();
//                String auth = new String(Base64.encodeBase64(origin.getBytes()));
//                connection.setRequestProperty("Authorization", new StringBuffer("Basic ").append(auth).toString());
//                connection.setRequestProperty("User-Agent", "MSIE 7.0");
//                connection.setConnectTimeout(5000);
//                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String line;
//                StringBuffer temp = new StringBuffer();
//                while (null != (line = reader.readLine())) {
//                    temp.append(line);
//                }
//                reader.close();
//                connection.disconnect();
//                JSONArray data = JSONArray.parseArray(temp.toString());
//                result.setData(data);
//                ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(new File(path)).usingAnyFreePort().build();
//                service.start();
//                WebDriver driver = new ChromeDriver();
//                WebDriver.Navigation navigation = driver.navigate();
//                new Thread(() -> login()).start();
//                navigation.to(url.toString());
//                WebElement element = driver.findElement(By.tagName("body"));
//                content = element.getText();
//                driver.close();
//                driver.quit();
//                service.stop();
//                break;
//            } catch (Exception e) {
//                logger.error(e.getMessage());
//                if (count <= 0) {
//                    logger.info("Fail to load the air quality information.");
//                }
//                cd--;
//            }
//        }
//        JSONArray data = new JSONArray();
//        try {
//            data = JSON.parseArray(content);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            logger.info(content);
//        }
//        List<CityAirQuality> list = new ArrayList<>();
//        CityAirQuality quality;
//        for (int i = 0; i < data.size(); i++) {
//            JSONObject item = data.getJSONObject(i);
//            quality = new CityAirQuality();
//            try {
//                quality.setCityId(item.getString("citycode"));
//                quality.setAqi(item.getDouble("aqi"));
//            } catch (Exception e) {
//                logger.info(JSON.toJSONString(item));
//                logger.error(quality.getCityId() + " cannot be updated at this time, skipping...");
//                continue;
//            }
//            if (quality.getAqi() > 300) {
//                quality.setAqiLevel("严重污染");
//            } else if (quality.getAqi() > 200) {
//                quality.setAqiLevel("重度污染");
//            } else if (quality.getAqi() > 150) {
//                quality.setAqiLevel("中度污染");
//            } else if (quality.getAqi() > 100) {
//                quality.setAqiLevel("轻度污染");
//            } else if (quality.getAqi() > 50) {
//                quality.setAqiLevel("良");
//            } else if (quality.getAqi() >= 0) {
//                quality.setAqiLevel("优");
//            }
//            try {
//                quality.setPrimePollution(item.getString("primarypollutant"));
//            } catch (Exception e) {
//                quality.setPrimePollution("——");
//            }
//            try {
//                quality.setPm2_5(item.getDouble("pm2_5"));
//            } catch (Exception e) {
//                quality.setPm2_5(0);
//            }
//            try {
//                quality.setPm10(item.getDouble("pm10"));
//            } catch (Exception e) {
//                quality.setPm10(0);
//            }
//            try {
//                quality.setCo(item.getDouble("co"));
//            } catch (Exception e) {
//                quality.setCo(0);
//            }
//            try {
//                quality.setNo2(item.getDouble("no2"));
//            } catch (Exception e) {
//                quality.setNo2(0);
//            }
//            try {
//                quality.setO3(item.getDouble("o3"));
//            } catch (Exception e) {
//                quality.setO3(0);
//            }
//            try {
//                quality.setSo2(item.getDouble("so2"));
//            } catch (Exception e) {
//                quality.setSo2(0);
//            }
//            quality.setRecordTime(new Timestamp(System.currentTimeMillis() / (3600000) * 3600000));
//            list.add(quality);
//        }
//        insertCityAqiDetail(list);
//        return result;
//    }
//
//    private void login() {
//        try {
//            Thread.sleep(8000);
//            stimulate(KeyEvent.VK_G, KeyEvent.VK_U, KeyEvent.VK_O, KeyEvent.VK_M, KeyEvent.VK_A, KeyEvent.VK_I);
//            stimulate(KeyEvent.VK_TAB);
//            Thread.sleep(100);
//            robot.keyPress(KeyEvent.VK_SHIFT);
//            robot.delay(100);
//            stimulate(KeyEvent.VK_G);
//            robot.delay(100);
//            robot.keyRelease(KeyEvent.VK_SHIFT);
//            robot.delay(100);
//            stimulate(KeyEvent.VK_M, KeyEvent.VK_A, KeyEvent.VK_I, KeyEvent.VK_R);
//            robot.keyPress(KeyEvent.VK_SHIFT);
//            robot.delay(100);
//            stimulate(KeyEvent.VK_2);
//            robot.delay(100);
//            robot.keyRelease(KeyEvent.VK_SHIFT);
//            robot.delay(100);
//            stimulate(KeyEvent.VK_2, KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_8);
//            stimulate(KeyEvent.VK_ENTER);
//            Thread.sleep(10000);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//    }
//
//    private void stimulate(int... code) {
//        for (int item : code) {
//            robot.keyPress(item);
//            robot.delay(100);
//            robot.keyRelease(item);
//            robot.delay(100);
//        }
//    }
//
//    private void insertCityAqiDetail(List<CityAirQuality> airQualityList) {
//        // step 1: update cache
//        try {
//            for (CityAirQuality cityAirQuality : airQualityList) {
//                airQualityCacheService.generate(cityAirQuality);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // step 2: update database
//        if (airQualityList.isEmpty())
//            return;
//        Timestamp timestamp = airQualityList.get(0).getRecordTime();
//        Map<String, Object> condition = new HashMap();
//        condition.put("recordTime", timestamp);
//        condition.put("blockFlag", false);
//        ResultData response = cityAirQualityDao.select(condition);
//        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
//            cityAirQualityDao.insertBatch(airQualityList);
//        }
//    }
//}


package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.CityAirQualityDao;
import finley.gmair.model.air.CityAirQuality;
import finley.gmair.model.air.MojiRecord;
import finley.gmair.model.air.MojiToken;
import finley.gmair.model.district.City;
import finley.gmair.model.district.District;
import finley.gmair.model.district.Province;
import finley.gmair.service.*;
import finley.gmair.service.feign.LocationFeign;
import finley.gmair.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @ClassName: CityAqiServiceMojiImpl
 * @Description: TODO
 * @Author fan
 * @Date 2019/10/10 12:09 AM
 */
@Service
public class CityAQIServiceImpl implements CityAQIService {
    private Logger logger = LoggerFactory.getLogger(CityAQIServiceImpl.class);

    @Autowired
    private ObscureCityService obscureCityService;

    @Autowired
    private ProvinceAirQualityService provinceAirQualityService;

    @Autowired
    private CityAirQualityDao cityAirQualityDao;

    @Autowired
    private AirQualityCacheService airQualityCacheService;

    @Autowired
    private MojiTokenService mojiTokenService;

    @Autowired
    public CityWeatherUtil cityWeatherUtil;


    private void rank() {
        new Thread(() -> {
            Map<String, CityAirQuality> map = new HashMap<>();
            for (Map.Entry<String, Province> entry : CityWeatherUtil.provinces.entrySet()) {
                String provinceId = entry.getKey();
                Province province = entry.getValue();
                CityAirQuality quality = fetch(provinceId, province.getLongitude(), province.getLatitude());
                if (quality == null) {
                    continue;
                }
                logger.info("Province aqi: " + JSON.toJSONString(quality));
                map.put(provinceId, quality);
            }
            logger.info("province aqi complete");
            for (Map.Entry<String, City> entry : CityWeatherUtil.cities.entrySet()) {
                String cityId = entry.getKey();
                City city = entry.getValue();
                CityAirQuality quality = fetch(cityId, city.getLongitude(), city.getLatitude());
                if (quality == null) {
                    continue;
                }
                logger.info("City aqi: " + JSON.toJSONString(quality));
                map.put(cityId, quality);
            }
            logger.info("city aqi complete");
            try {
                for (Map.Entry<String, District> entry : CityWeatherUtil.districts.entrySet()) {
                    String districtId = entry.getKey();
                    District district = entry.getValue();
                    MojiRecord record = locate(district.getDistrictName());
                    if (record == null) {
                        logger.info("Fail to find district: " + districtId);
                        continue;
                    }
                    logger.info("district fid: " + record.getFid());
                    CityAirQuality quality = fetch(districtId, record.getFid());
                    if (quality == null) {
                        continue;
                    }
                    logger.info("District aqi: " + JSON.toJSONString(quality));
                    map.put(districtId, quality);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("district aqi complete");
            List<CityAirQuality> data = map.values().stream().collect(Collectors.toList());
            insertCityAqiDetail(data);
        }).start();
    }

    @Override
    public ResultData obtain() {
        ResultData result = new ResultData();
        MojiToken mojiToken = ((List<MojiToken>) selectToken().getData()).get(0);
        logger.info("Active moji token: " + JSON.toJSONString(mojiToken));
        CityWeatherUtil.mojiToken = mojiToken;
        rank();
        return result;
    }

    //区使用该API进行获取
//    private CityAirQuality fetch(String lid, int cityId) {
//        MojiToken mojiToken = ((List<MojiToken>) selectToken().getData()).get(0);
//        String timestamp = "0";
//        StringBuffer sb = new StringBuffer(mojiToken.getUrl()).append("?timestamp=").append(timestamp).append("&cityId=").append(cityId).append("&token=").append(mojiToken.getToken()).append("&key=").append(Encryption.md5(mojiToken.getPassword() + timestamp));
//        logger.info(sb.toString());
//        String result = HttpDeal.getResponse(sb.toString());
//        CityAirQuality quality = interpret(lid, result);
//        return quality;
//    }

    private CityAirQuality fetch(String lid, int cityId) {
        String result = cityWeatherUtil.fetch(cityId);
        CityAirQuality quality = interpret(lid, result);
        return quality;
    }

    //省份和城市使用该API进行获取
//    private CityAirQuality fetch(String lid, double longitude, double latitude) {
//        MojiToken mojiToken = ((List<MojiToken>) selectToken().getData()).get(0);
//        String timestamp = "0";
//        StringBuffer sb = new StringBuffer(mojiToken.getUrl()).append("?timestamp=").append(timestamp).append("&lat=").append(latitude).append("&lon=").append(longitude).append("&token=").append(mojiToken.getToken()).append("&key=").append(Encryption.md5(mojiToken.getPassword() + timestamp));
//        logger.info(sb.toString());
//        String result = HttpDeal.getResponse(sb.toString());
//        CityAirQuality quality = interpret(lid, result);
//        return quality;
//    }

    private CityAirQuality fetch(String lid, double longitude, double latitude) {
        String result = cityWeatherUtil.fetch(longitude, latitude);
        CityAirQuality quality = interpret(lid, result);
        return quality;
    }

    //获取moji token
    ResultData selectToken() {
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", 0);
        return mojiTokenService.fetch(condition);
    }

    public CityAirQuality interpret(String id, String result) {
        JSONObject json = JSON.parseObject(result);
        int code = json.getInteger("code");
        // 先解析相应数据的响应代码，目前正确返回的数据code为200
        if (code != 200) {
            logger.error(json.toJSONString());
            return null;
        }
        if (!json.containsKey("data")) {
            logger.error("Current result is invalid: " + json.toJSONString());
            return null;
        }
        if (!json.getJSONObject("data").containsKey("aqi")) {
            logger.error("Current data is invalid: " + json.toJSONString());
            return null;
        }
        JSONObject data = json.getJSONObject("data").getJSONObject("aqi");
        CityAirQuality quality = new CityAirQuality();
        try {
            double aqi = data.getDouble("value");
            String level = data.getString("level");
            double pm2_5 = data.getDouble("pm25C");
            double pm10 = data.getDouble("pm10C");
            double co = data.getDouble("coC");
            double no2 = data.getDouble("no2C");
            double o3 = data.getDouble("o3C");
            double so2 = data.getDouble("so2C");
            quality.setCityId(id);
            quality.setAqi(aqi);
            quality.setAqiLevel(level);
            quality.setPm2_5(pm2_5);
            quality.setPm10(pm10);
            quality.setCo(co);
            quality.setNo2(no2);
            quality.setO3(o3);
            quality.setSo2(so2);
            quality.setRecordTime(new Timestamp(System.currentTimeMillis() / (3600000) * 3600000));
        } catch (Exception e) {
            return null;
        }
        return quality;
    }

    private void insertCityAqiDetail(List<CityAirQuality> airQualityList) {
        // step 1: update cache
        try {
            for (CityAirQuality cityAirQuality : airQualityList) {
                airQualityCacheService.generate(cityAirQuality);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // step 2: update database
        if (airQualityList.isEmpty()) {
            return;
        }
        Timestamp timestamp = airQualityList.get(0).getRecordTime();
        Map<String, Object> condition = new HashMap();
        condition.put("recordTime", timestamp);
        condition.put("blockFlag", false);
        ResultData response = cityAirQualityDao.select(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            cityAirQualityDao.insertBatch(airQualityList);
        }
    }

    private MojiRecord locate(String district) {
       return cityWeatherUtil.locate(district);
    }
}
