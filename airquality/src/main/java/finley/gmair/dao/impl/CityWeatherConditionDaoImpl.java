package finley.gmair.dao.impl;

import finley.gmair.dao.CityWeatherConditionDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.air.CityWeatherCondition;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.air.CityTempVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CityWeatherConditionDaoImpl extends BaseDao implements CityWeatherConditionDao {
    @Override
    public ResultData insertBatch(List<CityWeatherCondition> list) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.weathercondition.cityweathercondition.insertBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ResultData select(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<CityTempVo> list = sqlSession.selectList("gmair.weathercondition.cityweathercondition.select", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(list);
            }
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


}
