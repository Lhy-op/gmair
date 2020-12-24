package finley.gmair.dao;

import finley.gmair.model.air.CityAirQuality;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface CityAirQualityDao {

    ResultData insert(CityAirQuality airQuality);

    ResultData insertLatest(CityAirQuality airQuality);

    ResultData insertBatch(List<CityAirQuality> list);

    ResultData insertLatestBatch(List<CityAirQuality> list);

    ResultData select(Map<String, Object> condition);

    ResultData selectAqiFull(Map<String, Object> condition);
}
