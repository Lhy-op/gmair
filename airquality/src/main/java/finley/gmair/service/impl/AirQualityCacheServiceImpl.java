package finley.gmair.service.impl;

import finley.gmair.model.air.CityAirQuality;
import finley.gmair.service.AirQualityCacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class AirQualityCacheServiceImpl implements AirQualityCacheService{

        @Cacheable(value = {"airQualityMap"}, key = "#cityId", unless = "#result == null ")
        @Override
        public CityAirQuality fetch(String cityId) {
            return null;
        }
}
