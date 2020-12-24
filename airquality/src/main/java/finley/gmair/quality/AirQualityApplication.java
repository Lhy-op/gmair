package finley.gmair.quality;

import finley.gmair.service.CityAQIService;
import finley.gmair.service.MonitorStationCrawler;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@SpringBootApplication
@RestController
@ComponentScan({"finley.gmair.service", "finley.gmair.dao", "finley.gmair.controller","finley.gmair.util"})
@EnableFeignClients(basePackages = "finley.gmair.service")
@EnableScheduling
@EnableCaching
public class AirQualityApplication {
    public static void main(String[] args) {
        SpringApplication.run(AirQualityApplication.class, args);
    }

//    @Autowired
//    private RankCrawler rankCrawler;
//
//    @Autowired
//    private CityAqiService4Pm25ComImpl rankCrawler;

    @Autowired
    private MonitorStationCrawler monitorStationCrawler;

    @Autowired
    private CityAQIService cityAQIService;


    @PostMapping("/airquality/city/crawler")
    public ResultData crawler() {
        cityAQIService.obtain();
//        rankCrawler.rank();
        return new ResultData();
    }

    @PostMapping("/airquality/monitorStation/crawler")
    public ResultData monitorStationCrawler() {
//        monitorStationCrawler.craw();
        return new ResultData();
    }
}