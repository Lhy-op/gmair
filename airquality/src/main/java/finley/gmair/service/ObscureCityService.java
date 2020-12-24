package finley.gmair.service;

import finley.gmair.model.air.ObscureCity;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ObscureCityService {
    ResultData fetch(Map<String, Object> condition);

    ResultData assign(ObscureCity obscureCity);
}
