package finley.gmair.service;

import finley.gmair.model.air.CityAirQuality;
import finley.gmair.util.ResultData;
import org.springframework.cache.annotation.CachePut;

import java.util.Map;

public interface AirQualityCacheService{

    //这个airQualityMap缓存里存储的是cityId和CityAirQuality实体的对应关系.
    @CachePut(value = "airQualityMap", key = "#airQuality.cityId", condition = "#airQuality != null ")
    default CityAirQuality generate(CityAirQuality airQuality){
        return airQuality;
    }

    CityAirQuality fetch(String cityId);
}
