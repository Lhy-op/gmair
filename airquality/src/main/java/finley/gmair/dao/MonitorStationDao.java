package finley.gmair.dao;

import finley.gmair.model.air.MonitorStation;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface MonitorStationDao {

    ResultData insert(MonitorStation monitorStation);
    ResultData insertBatch(List<MonitorStation> list);

    ResultData fetch(Map<String, Object> condition);
    ResultData empty();
}
