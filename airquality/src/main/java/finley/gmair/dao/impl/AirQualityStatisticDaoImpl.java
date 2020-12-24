package finley.gmair.dao.impl;

import finley.gmair.dao.AirQualityStatisticDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.air.CityAirQualityStatistic;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.air.CityAirQualityStatisticVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AirQualityStatisticDaoImpl extends BaseDao implements AirQualityStatisticDao{

    @Override
    public ResultData insertHourlyData(List<CityAirQualityStatistic> list) {
        ResultData resultData = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.airqualitystatistic.insertHourlyBatch", list);
            resultData.setData(list);
        } catch (Exception e) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return resultData;
    }

    @Override
    public ResultData insertDailyData(List<CityAirQualityStatistic> list) {
        ResultData resultData = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.airqualitystatistic.insertDailyBatch", list);
            resultData.setData(list);
        } catch (Exception e) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return resultData;
    }

    @Override
    public ResultData insertMonthlyData(List<CityAirQualityStatistic> list) {
        ResultData resultData = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.airqualitystatistic.insertMonthlyBatch", list);
            resultData.setData(list);
        } catch (Exception e) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return resultData;
    }

    @Override
    public ResultData fetchHourlyData(Map<String, Object> condition) {
        ResultData resultData = new ResultData();
        try {
            List<CityAirQualityStatisticVo> list =
                    sqlSession.selectList("gmair.airquality.airqualitystatistic.selectHourly", condition);
            if (list.isEmpty()) {
                resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                resultData.setData(list);
            }
        } catch (Exception e) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return resultData;
    }

    @Override
    public ResultData fetchDailyData(Map<String, Object> condition) {
        ResultData resultData = new ResultData();
        try {
            List<CityAirQualityStatisticVo> list =
                    sqlSession.selectList("gmair.airquality.airqualitystatistic.selectDaily", condition);
            if (list.isEmpty()) {
                resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                resultData.setData(list);
            }
        } catch (Exception e) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return resultData;
    }

    @Override
    public ResultData fetchMonthlyData(Map<String, Object> condition) {
        ResultData resultData = new ResultData();
        try {
            List<CityAirQualityStatisticVo> list =
                    sqlSession.selectList("gmair.airquality.airqualitystatistic.selectMonthly", condition);
            if (list.isEmpty()) {
                resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                resultData.setData(list);
            }
        } catch (Exception e) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return resultData;
    }
}
