package finley.gmair.controller;

import finley.gmair.service.ProvinceAirQualityService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/airquality")
public class ProvinceAirQualityController {

    @Autowired
    private ProvinceAirQualityService provinceAirQualityService;

    @CrossOrigin
    @GetMapping("/province/list")
    ResultData provinceAirQualityList() {
        ResultData result = new ResultData();

        Map<String, Object> condition = new HashMap<>();
        ResultData response = provinceAirQualityService.fetch(condition);
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
    @GetMapping("/province/{provinceId}")
    public ResultData provinceAirQuality(@PathVariable("provinceId") String provinceId){
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("provinceId",provinceId);
        condition.put("blockFlag",false);
        ResultData response = provinceAirQualityService.fetch(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find airquality by provinceId");
            return result;
        } else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the provinceId");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to fetch the air quality");
        return result;
    }
}
