package finley.gmair.dao;

import finley.gmair.model.air.MonitorStationAirQuality;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface MonitorStationAirQualityDao {
    ResultData insert(MonitorStationAirQuality monitorStationAirQuality);
    ResultData insertBatch(List<MonitorStationAirQuality> list);
    ResultData insertLatest(MonitorStationAirQuality monitorStationAirQuality);
    ResultData insertLatestBatch(List<MonitorStationAirQuality> list);
    ResultData selectLatest(Map<String, Object> condition);
}
