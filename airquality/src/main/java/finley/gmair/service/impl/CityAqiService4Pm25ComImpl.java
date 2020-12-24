//package finley.gmair.service.impl;
//
//import finley.gmair.dao.CityAirQualityDao;
//import finley.gmair.model.air.CityAirQuality;
//import finley.gmair.model.air.ObscureCity;
//import finley.gmair.service.AirQualityCacheService;
//import finley.gmair.service.CityAQIService;
//import finley.gmair.service.ObscureCityService;
//import finley.gmair.service.ProvinceAirQualityService;
//import finley.gmair.util.ResponseCode;
//import finley.gmair.util.ResultData;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.sql.Timestamp;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//
///**
// * @ClassName: RankCrawler4PM25COM
// * @Description: TODO
// * @Author fan
// * @Date 2019/6/16 10:27 AM
// */
//@Service
//public class CityAqiService4Pm25ComImpl implements CityAQIService {
//    private static Logger logger = LoggerFactory.getLogger(CityAqiService4Pm25ComImpl.class);
//
//    private final static String AIR_RANK = "http://pm25.com/rank.html";
//
//
//    @Autowired
//    private ObscureCityService obscureCityService;
//
//    @Autowired
//    private ProvinceAirQualityService provinceAirQualityService;
//
//    @Autowired
//    private CityAirQualityDao cityAirQualityDao;
//
//    @Autowired
//    private AirQualityCacheService airQualityCacheService;
//
//    public void rank() {
//        int count = 1;
//        Map<String, CityAirQuality> map = new HashMap<>();
//        //获取城市信息
//        while (count > 0) {
//            try {
//                Document page = Jsoup.connect(AIR_RANK).get();
//                Elements list = page.select("ul.rank_box").select("li");
//                ResultData response;
//                int i = 0;
//                for (Element item : list) {
//                    CityAirQuality quality = new CityAirQuality();
//                    String city = item.select("a.pjadt_location").text();
//                    //获取city对应的信息
//                    Map<String, Object> condition = new HashMap<>();
//                    condition.put("cityName", city);
//                    condition.put("blockFlag", false);
//                    response = obscureCityService.fetch(condition);
//                    if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
//                        logger.info("cannot resolve airquality for city: " + city);
//                        continue;
//                    }
//                    String aqi = item.select("span.pjadt_aqi").text().trim();
//                    String level = item.select("span.pjadt_quality>em").text().trim();
//                    String pm2_5 = item.select("span.pjadt_pm25").text().replace("μg/m³", "").trim();
//                    ObscureCity oc = ((List<ObscureCity>) response.getData()).get(0);
//                    quality.setCityId(oc.getCityId());
//                    quality.setAqi(Double.parseDouble(aqi));
//                    quality.setAqiLevel(level);
//                    quality.setPm2_5(Double.parseDouble(pm2_5));
//                    quality.setRecordTime(new Timestamp(System.currentTimeMillis() / (3600000) * 3600000));
//                    map.put(oc.getCityId(), quality);
//                }
//                count--;
//            } catch (Exception e) {
//                logger.error(e.getMessage());
//            }
//        }
//        List<CityAirQuality> data = map.values().stream().collect(Collectors.toList());
//        insertCityAqiDetail(data);
////        provinceAirQualityService.generate(data);
//    }
//
//    @Override
//    public ResultData obtain() {
//        ResultData result = new ResultData();
//        rank();
//        return result;
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
