package finley.gmair.dao;

import finley.gmair.model.air.CityWeatherCondition;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface CityWeatherConditionDao {
    ResultData insertBatch(List<CityWeatherCondition> list);

    ResultData select(Map<String, Object> condition);

}
