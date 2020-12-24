package finley.gmair.service;

import finley.gmair.dao.CityAirQualityDao;
import finley.gmair.dao.CityUrlDao;
import finley.gmair.dao.MonitorStationDao;
import finley.gmair.model.air.CityAirQuality;
import finley.gmair.model.air.CityUrl;
import finley.gmair.model.air.MonitorStation;
import finley.gmair.model.air.ObscureCity;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.air.CityUrlVo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RankCrawler {
    private final static String AIR_RANK = "http://pm25.in/rank";
    private final static String AIR_URL = "http://pm25.in";

    @Autowired
    ObscureCityCacheService obscureCityCacheService;

    @Autowired
    CityUrlDao cityUrlDao;

    @Autowired
    CityAirQualityDao airQualityDao;

    @Autowired
    MonitorStationDao monitorStationDao;

    @Autowired
    AirQualityCacheService airQualityCacheService;

    @Autowired
    private ProvinceAirQualityService provinceAirQualityService;

    /**
     * get city rank and
     * every hour on half
     */
    public void rank() {
        Map<String, CityAirQuality> map = new HashMap<>();
        int count = 1;
        while (count > 0) {
            try {
                Document page = Jsoup.connect(AIR_RANK).get();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis() / (3600000) * 3600000);

                Element rankTable = page.select("div.table").first();
                Element tableBody = rankTable.getElementsByTag("tbody").first();
                Elements trs = tableBody.getElementsByTag("tr");
                for (Element tr : trs) {
                    Elements tds = tr.getElementsByTag("td");
                    CityAirQuality airQuality = new CityAirQuality();
                    try {
                        Element cityHref = tds.get(1).getElementsByTag("a").first();
                        String obscureCityName = cityHref.text();
                        ObscureCity obscureCity = obscureCityCacheService.fetch(obscureCityName);
                        if (obscureCity != null) {
                            // if we have city name in the cache
                            String cityId = obscureCity.getCityId();
                            airQuality.setCityId(cityId);
                        }
                        airQuality.setAqi(Double.parseDouble(tds.get(2).text()));
                        airQuality.setAqiLevel(tds.get(3).text());
                        airQuality.setPrimePollution(tds.get(4).text());
                        airQuality.setPm2_5(Double.parseDouble(tds.get(5).text()));
                        airQuality.setPm10(Double.parseDouble(tds.get(6).text()));
                        airQuality.setCo(Double.parseDouble(tds.get(7).text()));
                        airQuality.setNo2(Double.parseDouble(tds.get(8).text()));
                        airQuality.setO3(Double.parseDouble(tds.get(9).text()));
                        airQuality.setSo2(Double.parseDouble(tds.get(11).text()));
                        airQuality.setRecordTime(timestamp);
                        if (airQuality.getCityId() != null) {
                            map.put(cityHref.text(), airQuality);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                count--;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                //another try in 1 minutes for 4 times
                try {
                    Thread.sleep(60000);
                } catch (Exception te) {

                }
            }
        }
        List<CityAirQuality> airQualityList = map.values().stream().collect(Collectors.toList());

        //把爬取到的空气质量数据存入缓存airQualityMap,key为cityId,并插入city_aqi_full表中
//        insertCityAqiDetail(airQualityList);

        //根据城市pm25和aqiIndex计算平均值,统计出city对应province的数据,记录到province_airquality表中
        provinceAirQualityService.generate(airQualityList);
    }

//    private void insertCityAqiDetail(List<CityAirQuality> airQualityList) {
//        // step 1: update cache
//        try {
//            for (CityAirQuality cityAirQuality : airQualityList) {
//                airQualityCacheService.generate(cityAirQuality);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        // step 2: update database
//        if (airQualityList.isEmpty())
//            return;
//        Timestamp timestamp = airQualityList.get(0).getRecordTime();
//        Map<String, Object> condition = new HashMap();
//        condition.put("recordTime", timestamp);
//        condition.put("blockFlag", false);
//        ResultData response = airQualityDao.select(condition);
//        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
//            airQualityDao.insertBatch(airQualityList);
//        }
//    }

    private void updateCityUrl(List<CityUrl> cityUrlList) {
        if (!cityUrlList.isEmpty())
            cityUrlDao.replaceBatch(cityUrlList);
    }


}
