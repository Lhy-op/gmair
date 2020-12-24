package finley.gmair.service;

import finley.gmair.model.air.CityAirQuality;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface ProvinceAirQualityService {

    ResultData generate(List<CityAirQuality> list);
    ResultData fetch(Map<String, Object> condition);
}
