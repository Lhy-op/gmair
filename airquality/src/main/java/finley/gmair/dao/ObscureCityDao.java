package finley.gmair.dao;

import finley.gmair.model.air.ObscureCity;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ObscureCityDao {
    ResultData query(Map<String, Object> condition);
    ResultData replace(ObscureCity obscureCity);
}
