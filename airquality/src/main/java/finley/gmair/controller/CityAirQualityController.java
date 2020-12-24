package finley.gmair.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;
import finley.gmair.model.air.MojiToken;
import finley.gmair.service.AirQualityStatisticService;
import finley.gmair.service.MojiTokenService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.TimeUtil;
import finley.gmair.vo.air.CityAirQualityStatisticVo;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/airquality")
public class CityAirQualityController {

    @Autowired
    private AirQualityStatisticService airQualityStatisticService;

    @Autowired
    private MojiTokenService mojiTokenService;

    @GetMapping(value = "/latest")
    public ResultData getLatestCityAirQuality() {
        return airQualityStatisticService.fetchLatestAirQuality(new HashMap<>());
    }

    @GetMapping(value = "/latest/{cityId}")
    public ResultData getLatestCityAirQuality(@PathVariable String cityId) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityId", cityId);
        return airQualityStatisticService.fetchLatestAirQuality(condition);
    }

    @RequestMapping(value = "/hourly/cityAqi", method = RequestMethod.POST)
    public ResultData scheduleHourlyCityAqi() {
        return airQualityStatisticService.handleAirQualityHourlyStatistic();
    }

    @GetMapping(value = "/hourly/cityAqi")
    public ResultData getHourlyCityAqi() {

        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        Timestamp lastHourTime = new Timestamp(System.currentTimeMillis() - 3600000);
        condition.put("createTimeGTE", lastHourTime);

        ResultData response = airQualityStatisticService.fetchAirQualityHourlyStatistic(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("The airQuality server is busy, please try again");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(value = "/hourly/cityAqi/{cityId}", method = RequestMethod.GET)
    public ResultData getHourlyCityAqi(@PathVariable("cityId") String cityId) {

        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();

        Timestamp last24Hour = new Timestamp(System.currentTimeMillis() / (1000 * 60 * 60) * (1000 * 60 * 60) - 24 * 60 * 60 * 1000);
        Timestamp lastHour = new Timestamp(System.currentTimeMillis() / (1000 * 60 * 60) * (1000 * 60 * 60));
        condition.put("cityId", cityId);
        condition.put("createTimeGTE", last24Hour);
        condition.put("createTimeLTE", lastHour);
        ResultData response = airQualityStatisticService.fetchAirQualityHourlyStatistic(condition);

        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the airquality server is busy, try later!");
            return result;
        } else {
            JSONArray jsonArray = (JSONArray) response.getData();
            List<CityAirQualityStatisticVo> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(jsonArray.get(i));
                CityAirQualityStatisticVo caqsvo = jsonObject.toJavaObject(CityAirQualityStatisticVo.class);
                caqsvo.setCreateTime(new Timestamp(caqsvo.getCreateTime().getTime() / (1000 * 60 * 60) * (1000 * 60 * 60)));
                list.add(caqsvo);
            }
            for (int i = 0; i < 24; i++) {
                if (list.size() == i || list.get(i).getCreateTime().getTime() != last24Hour.getTime() + (i + 1) * 1000 * 60 * 60) {
                    list.add(i, new CityAirQualityStatisticVo(cityId, 0, new Timestamp(last24Hour.getTime() + (i + 1) * 1000 * 60 * 60)));
                }
            }
            result.setData(list);
        }
        return result;
    }

    @RequestMapping(value = "/daily/cityAqi", method = RequestMethod.POST)
    public ResultData scheduleDailyCityAqi() {
        return airQualityStatisticService.handleAirQualityDailyStatistic();
    }

    @RequestMapping(value = "/daily/cityAqi/{cityId}", method = RequestMethod.GET)
    public ResultData getDailyCityAqi(@PathVariable("cityId") String cityId) {
        ResultData result = new ResultData();

        //通过Calendar获取今日零点零分零秒的毫秒数
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long zero = cal.getTimeInMillis(); // 今天零点零分零秒的毫秒数
        Timestamp todayZero = new Timestamp(zero);
        Timestamp last7dayZero = new Timestamp(zero - 7 * 24 * 60 * 60 * 1000);

        //查询过去七天的pm2.5记录
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityId", cityId);
        condition.put("createTimeGTE", last7dayZero);
        condition.put("createTimeLTE", todayZero);
        ResultData response = airQualityStatisticService.fetchAirQualityDailyStatistic(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch city daily aqi");
            return result;
        } else {
            //将查询结果格式化
            List<CityAirQualityStatisticVo> list = (List<CityAirQualityStatisticVo>) response.getData();
            for (int i = 0; i < list.size(); i++) {
                long thatTime = list.get(i).getCreateTime().getTime();
                list.get(i).setCreateTime(new Timestamp(thatTime - (thatTime + 8 * 60 * 60 * 1000) % (24 * 60 * 60 * 1000)));
            }
            for (int i = 0; i < 7; i++) {
                if (list.size() == i || list.get(i).getCreateTime().getTime() != last7dayZero.getTime() + (i + 1) * 1000 * 60 * 60 * 24) {
                    list.add(i, new CityAirQualityStatisticVo(cityId, 0, new Timestamp(last7dayZero.getTime() + (i + 1) * 1000 * 60 * 60 * 24)));
                }
            }
            result.setData(list);
        }
        return result;
    }

    @RequestMapping(value = "/monthly/cityAqi", method = RequestMethod.POST)
    public ResultData scheduleMonthlyCityAqi() {

        return airQualityStatisticService.handleAirQualityMonthlyStatistic();
    }

    @RequestMapping(value = "/monthly/cityAqi/{cityId}", method = RequestMethod.GET)
    public ResultData getMonthlyCityAqi(@PathVariable("cityId") String cityId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityId", cityId);

        ResultData response = airQualityStatisticService.fetchAirQualityMonthlyStatistic(condition);

        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the airquality server is busy, try later!");
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @CrossOrigin
    @GetMapping("/weekly/cityAqi/{cityId}")
    public ResultData getWeeklyCityAqi(@PathVariable String cityId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityId", cityId);
        LocalDate today = LocalDateTime.now().toLocalDate();
        LocalDate lastWeekDay = today.minusDays(7);
        condition.put("createTimeGTE", lastWeekDay);

        ResultData response = airQualityStatisticService.fetchAirQualityDailyStatistic(condition);

        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the airquality server is busy, try later!");
        } else {
            result.setData(response.getData());
        }
        return result;
    }


    private ResultData getFormatedData(String cityId, JSONArray dataList, int listLength, int timeType) {
        Timestamp cur;
        Timestamp last;
        long timeInteval = 0;
        //0代表格式化daily data
        if (timeType == 0) {
            timeInteval = 24 * 60 * 60 * 1000;
            cur = TimeUtil.getTodayZeroTimestamp();
            last = new Timestamp(cur.getTime() - ((long)(listLength - 1)) * timeInteval);
        }
        //其他代表格式化hourly data
        else {
            timeInteval = 60 * 60 * 1000;
            cur = TimeUtil.getThatTimeStampHourTimestamp(new Timestamp(System.currentTimeMillis()));
            last = new Timestamp(cur.getTime() - (listLength - 1) * timeInteval);
        }
        ResultData result = new ResultData();
        JSONArray resultList = new JSONArray();
        for (int i = 0; i < listLength; i++) {
            JSONObject temp = new JSONObject();
            temp.put("city", cityId);
            temp.put("createTime", new Timestamp(last.getTime() + i * timeInteval).toString());
            resultList.add(temp);
        }
        for (int i = 0; i < dataList.size(); i++) {
            JSONObject curObj = dataList.getJSONObject(i);
            Timestamp thatHour = TimeUtil.getThatTimeStampHourTimestamp(curObj.getTimestamp("createTime"));
            long diff = (thatHour.getTime() - last.getTime()) / timeInteval;
            if (diff < 0 || diff >= listLength) {
                continue;
            }
            for (String key : curObj.keySet()) {
                if (key.contains("pm25")) {
                    resultList.getJSONObject((int) diff).put(key, curObj.get(key));
                }
            }
        }
        for (int i = 0; i < listLength; i++) {
            JSONObject jsonObject = resultList.getJSONObject(i);
            for (String key : dataList.getJSONObject(0).keySet()) {
                if (!jsonObject.containsKey(key)) {
                    jsonObject.put(key, 0);
                }
            }
        }
        result.setData(resultList);
        return result;
    }

    //获取过去N天的室外pm2.5记录
    @GetMapping("/lastNday")
    public ResultData fetchLastNDayData(String cityId, int lastNday) {
        ResultData result = new ResultData();
        if (org.springframework.util.StringUtils.isEmpty(cityId) || org.springframework.util.StringUtils.isEmpty(lastNday)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide cityId and lastNday");
            return result;
        }

        //查询数据库获取室内的过去N天的数据
        Timestamp todayZero = TimeUtil.getTodayZeroTimestamp();
        Timestamp lastNDayZero = new Timestamp(todayZero.getTime() - (long) (lastNday - 1) * 24 * 60 * 60 * 1000);
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityId", cityId);
        condition.put("createTimeGTE", lastNDayZero);
        condition.put("blockFlag", false);
        ResultData response = airQualityStatisticService.fetchAirQualityDailyStatistic(condition);
        //根据不同的数据类型查不同的表
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription("can't get any data");
            return result;
        }
        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(response.getData()));
        return getFormatedData(cityId, jsonArray, lastNday, 0);
    }

    //获取machine过去N小时的pm2.5记录
    @GetMapping("/lastNhour")
    public ResultData fetchLastNHourData(String cityId, int lastNhour) {
        ResultData result = new ResultData();
        if (org.springframework.util.StringUtils.isEmpty(cityId) || org.springframework.util.StringUtils.isEmpty(lastNhour)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide cityId and lastNhour");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        Timestamp curHourZero = TimeUtil.getThatTimeStampHourTimestamp(new Timestamp(System.currentTimeMillis()));
        Timestamp lastNHourZero = new Timestamp(curHourZero.getTime() - (lastNhour - 1) * 60 * 60 * 1000);
        condition.clear();
        condition.put("cityId", cityId);
        condition.put("createTimeGTE", lastNHourZero);
        condition.put("blockFlag", false);
        ResultData response = airQualityStatisticService.fetchAirQualityHourlyStatistic(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription("can't get any data");
            return result;
        }
        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(response.getData()));
        return getFormatedData(cityId, jsonArray, lastNhour, 1);
    }

    @GetMapping("/token/select")
    ResultData selectToken() {
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", 0);
        return mojiTokenService.fetch(condition);
    }

    @PostMapping("/token/create")
    ResultData createToken(String token, String url, String password, String base) {
        MojiToken mojiToken = new MojiToken(token, password, url, base);
        return mojiTokenService.create(mojiToken);
    }
}
