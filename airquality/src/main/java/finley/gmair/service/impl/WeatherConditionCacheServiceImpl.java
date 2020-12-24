package finley.gmair.service.impl;

import finley.gmair.model.air.CityWeatherCondition;
import finley.gmair.service.WeatherConditionCacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class WeatherConditionCacheServiceImpl implements WeatherConditionCacheService {

    @Cacheable(value = {"weatherConditionMap"}, key = "#cityId", unless = "#result == null ")
    @Override
    public CityWeatherCondition fetch(String cityId) {
        return null;
    }
}
