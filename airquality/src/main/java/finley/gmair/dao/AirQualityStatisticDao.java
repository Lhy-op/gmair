package finley.gmair.dao;

import finley.gmair.model.air.CityAirQualityStatistic;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface AirQualityStatisticDao {

    ResultData insertHourlyData(List<CityAirQualityStatistic> list);
    ResultData insertDailyData(List<CityAirQualityStatistic> list);
    ResultData insertMonthlyData(List<CityAirQualityStatistic> list);

    ResultData fetchHourlyData(Map<String, Object> condition);
    ResultData fetchDailyData(Map<String, Object> condition);
    ResultData fetchMonthlyData(Map<String, Object> condition);
}
