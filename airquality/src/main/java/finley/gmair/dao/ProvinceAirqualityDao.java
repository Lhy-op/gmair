package finley.gmair.dao;

import finley.gmair.model.air.ProvinceAirQuality;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface ProvinceAirqualityDao {

    ResultData insert(ProvinceAirQuality provinceAirQuality);
    ResultData insertBatch(List<ProvinceAirQuality> provinceAirQualities);
    ResultData select(Map<String, Object> condition);
}
