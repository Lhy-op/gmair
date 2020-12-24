package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.AirQualityStatisticDao;
import finley.gmair.dao.CityAirQualityDao;
import finley.gmair.model.air.AirQuality;
import finley.gmair.model.air.CityAirQuality;
import finley.gmair.model.air.CityAirQualityStatistic;
import finley.gmair.model.air.CityAqiFull;
import finley.gmair.service.AirQualityCacheService;
import finley.gmair.service.AirQualityStatisticService;
import finley.gmair.service.ProvinceCityCacheService;
import finley.gmair.service.feign.LocationFeign;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.air.CityAirPm25Vo;
import finley.gmair.vo.air.CityAirQualityStatisticVo;
import finley.gmair.vo.location.CityProvinceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AirQualityStatisticServiceImpl implements AirQualityStatisticService {

    @Autowired
    private AirQualityStatisticDao airQualityStatisticDao;

    @Autowired
    private CityAirQualityDao cityAirQualityDao;

    @Autowired
    private ProvinceCityCacheService provinceCityCacheService;

    @Autowired
    private AirQualityCacheService airQualityCacheService;

    @Autowired
    private LocationFeign locationFeign;

    @Override
    public ResultData handleAirQualityHourlyStatistic() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        Timestamp lastHour = new Timestamp((System.currentTimeMillis() - 300000) / (3600000) * 3600000);
        condition.put("recordTime", lastHour);
        //从city_aqi_full表中查找数据
        ResultData response = cityAirQualityDao.select(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CityAirPm25Vo> list = (List<CityAirPm25Vo>) response.getData();
            Map<String, Double> cityAqiMap = list.stream().collect(
                    Collectors.groupingBy(CityAirPm25Vo::getCityId,
                            Collectors.averagingDouble(CityAirPm25Vo::getPm25)));

            List<CityAirQualityStatistic> airQualityStatisticList = cityAqiMap.entrySet().stream()
                    .map(e -> new CityAirQualityStatistic(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

            if (!airQualityStatisticList.isEmpty())
                response = airQualityStatisticDao.insertHourlyData(airQualityStatisticList);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            LocalDateTime localDateTime = lastHour.toLocalDateTime();
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no hourly data in city air quality, " + localDateTime.toString());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData handleAirQualityDailyStatistic() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        LocalDate lastDate = new Timestamp(System.currentTimeMillis()).toLocalDateTime().toLocalDate().minusDays(1);

        condition.put("createTimeGTE", LocalDateTime.of(lastDate, LocalTime.MIN));
        condition.put("createTimeLTE", LocalDateTime.of(lastDate, LocalTime.MAX));

        ResultData response = airQualityStatisticDao.fetchHourlyData(condition);

        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CityAirQualityStatisticVo> list = (List<CityAirQualityStatisticVo>) response.getData();
            Map<String, Double> cityAqiMap = list.stream().collect(
                    Collectors.groupingBy(CityAirQualityStatisticVo::getCityId,
                            Collectors.averagingDouble(CityAirQualityStatisticVo::getPm25)));

            List<CityAirQualityStatistic> airQualityStatisticList = cityAqiMap.entrySet().stream()
                    .map(e -> new CityAirQualityStatistic(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

            if (!airQualityStatisticList.isEmpty())
                response = airQualityStatisticDao.insertDailyData(airQualityStatisticList);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no daily data in city air quality, " + lastDate.toString());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData handleAirQualityMonthlyStatistic() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        LocalDateTime localDateTime = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
        LocalDateTime lastMonth = localDateTime.withMonth(localDateTime.getMonth().minus(1).getValue());

        condition.put("createTimeGTE", localDateTime);
        condition.put("createTimeLTE", localDateTime);
        ResultData response = airQualityStatisticDao.fetchDailyData(condition);

        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CityAirQualityStatisticVo> list = (List<CityAirQualityStatisticVo>) response.getData();
            Map<String, Double> cityAqiMap = list.stream().collect(
                    Collectors.groupingBy(CityAirQualityStatisticVo::getCityId,
                            Collectors.averagingDouble(CityAirQualityStatisticVo::getPm25)));

            List<CityAirQualityStatistic> airQualityStatisticList = cityAqiMap.entrySet().stream()
                    .map(e -> new CityAirQualityStatistic(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

            if (!airQualityStatisticList.isEmpty())
                response = airQualityStatisticDao.insertMonthlyData(airQualityStatisticList);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no monthly data in city air quality, " + lastMonth.toString());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchLatestAirQuality(Map<String, Object> condition) {
        ResultData result = new ResultData();
        List<JSONObject> list = new LinkedList<>();
        if (condition.keySet().contains("cityId")) {
            CityAirQuality cityAirQuality = airQualityCacheService.fetch((String) condition.get("cityId"));
            if (cityAirQuality != null) {
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(cityAirQuality);
                String cityId = cityAirQuality.getCityId();
                String cityName = obtainCityName(cityId);
                if (!StringUtils.isEmpty(cityName)) {
                    jsonObject.put("cityName", cityName);
                }
                list.add(jsonObject);
            } else {
                ResultData response = cityAirQualityDao.selectAqiFull(condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    CityAqiFull aqiFull = ((List<CityAqiFull>) response.getData()).get(0);
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(aqiFull);
                    String cityId = aqiFull.getCityId();
                    String cityName = obtainCityName(cityId);
                    if (!StringUtils.isEmpty(cityName)) {
                        jsonObject.put("cityName", cityName);
                    }
                    list.add(jsonObject);
                }
            }
        } else {
            Set<String> citySet = provinceCityCacheService.getAvailableCity();
            for (String cityId : citySet) {
                CityAirQuality cityAirQuality = airQualityCacheService.fetch(cityId);
                if (cityAirQuality != null) {
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(cityAirQuality);
                    String cityName = obtainCityName(cityId);
                    if (!StringUtils.isEmpty(cityName)) {
                        jsonObject.put("cityName", cityName);
                    }
                    list.add(jsonObject);
                } else {
                    condition.clear();
                    condition.put("cityId", cityId);
                    condition.put("blockFlag", false);
                    ResultData response = cityAirQualityDao.selectAqiFull(condition);
                    if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                        CityAqiFull aqiFull = ((List<CityAqiFull>) response.getData()).get(0);
                        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(aqiFull);
                        String cityName = obtainCityName(cityId);
                        if (!StringUtils.isEmpty(cityName)) {
                            jsonObject.put("cityName", cityName);
                        }
                        list.add(jsonObject);
                    }
                }
            }
        }
        if (list.isEmpty()) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else {
            result.setData(list);
        }
        return result;
    }

    @Override
    public ResultData fetchAirQualityHourlyStatistic(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = airQualityStatisticDao.fetchHourlyData(condition);

        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<Object> list = (List<Object>) response.getData();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(list.get(i));
                String cityId = jsonObject.getString("cityId");
                String cityName = obtainCityName(cityId);
                if (!StringUtils.isEmpty(cityName)) {
                    jsonObject.put("cityName", cityName);
                }
                jsonArray.add(jsonObject);
            }
            result.setData(jsonArray);
        } else {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        }

        return result;
    }


    @Override
    public ResultData fetchAirQualityDailyStatistic(Map<String, Object> condition) {
        return airQualityStatisticDao.fetchDailyData(condition);
    }

    @Override
    public ResultData fetchAirQualityMonthlyStatistic(Map<String, Object> condition) {
        return airQualityStatisticDao.fetchMonthlyData(condition);
    }

    private String obtainCityName(String cityId) {
        ResultData response = locationFeign.detail(cityId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            JSONObject detail = JSONArray.parseArray(JSON.toJSONString(response.getData())).getJSONObject(0);
            return detail.containsKey("cityName") ? detail.getString("cityName") : null;
        } else {
            return null;
        }
    }
}
