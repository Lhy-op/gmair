package finley.gmair.controller;


import finley.gmair.service.MonitorStationCrawler;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/airquality")
public class MonitorStationAirQualityController {

    @Autowired
    private MonitorStationCrawler monitorStationCrawler;

    @GetMapping("/station/{stationId}")
    ResultData fetchLatestByStationId(@PathVariable String stationId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("stationId", stationId);
        condition.put("blockFlag", false);
        ResultData response = monitorStationCrawler.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器忙，请稍后再试");
        } else {
            result.setData(response.getData());
        }

        return result;
    }

    @CrossOrigin
    @GetMapping("/station/city/{cityId}")
    ResultData fetchLatestByCityId(@PathVariable String cityId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("cityId", cityId);
        condition.put("blockFlag", false);
        ResultData response = monitorStationCrawler.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器忙，请稍后再试");
        } else {
            result.setData(response.getData());
        }

        return result;
    }

    @GetMapping("/station/list")
    ResultData fetchLatestStationList() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = monitorStationCrawler.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器忙，请稍后再试");
        } else {
            result.setData(response.getData());
        }

        return result;
    }
}
