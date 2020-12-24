package finley.gmair.dao;

import finley.gmair.model.air.CityUrl;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface CityUrlDao {

    ResultData replace(CityUrl cityUrl);

    ResultData replaceBatch(List<CityUrl> cityUrlList);

    ResultData select(Map<String, Object> condition);
}
