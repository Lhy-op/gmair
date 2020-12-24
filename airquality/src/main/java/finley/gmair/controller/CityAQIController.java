package finley.gmair.controller;

import finley.gmair.service.CityAQIService;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/airquality/city")
public class CityAQIController {
    private Logger logger = LoggerFactory.getLogger(CityAQIController.class);

    @Autowired
    private CityAQIService cityAQIService;

    /**
     * 调用该接口重新获取最新的空气质量数据
     *
     * @return
     */
    @GetMapping("/aqi/refresh")
    public ResultData refresh() {
        ResultData result = new ResultData();
        new Thread(() -> cityAQIService.obtain()).start();
        result.setDescription("已开始获取城市空气信息，请稍后");
        return result;
    }
}
