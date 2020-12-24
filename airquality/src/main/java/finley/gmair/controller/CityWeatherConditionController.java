package finley.gmair.controller;

import finley.gmair.service.CityWeatherConditionService;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weathercondition/city")
public class CityWeatherConditionController {
    private Logger Logger = LoggerFactory.getLogger(CityWeatherConditionController.class);

    @Autowired
    CityWeatherConditionService cityWeatherConditionService;

    @GetMapping("/condition/refresh")
    public ResultData refresh() {
        ResultData result = new ResultData();
        new Thread(() -> cityWeatherConditionService.obtain()).start();
        result.setDescription("已开始获取城市天气信息，请稍后");
        return result;
    }
}
