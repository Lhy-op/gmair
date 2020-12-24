package finley.gmair.service;

import finley.gmair.model.air.CityWeatherCondition;
import org.springframework.cache.annotation.CachePut;

public interface WeatherConditionCacheService {
    @CachePut(value = "weatherConditionMap", key = "#weatherCondition.cityId", condition = "#weatherCondition != null ")
    default CityWeatherCondition generate(CityWeatherCondition weatherCondition){
        return weatherCondition;
    }

    CityWeatherCondition fetch(String cityId);
}
