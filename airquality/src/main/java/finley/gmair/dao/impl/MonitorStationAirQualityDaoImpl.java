package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MonitorStationAirQualityDao;
import finley.gmair.model.air.MonitorStationAirQuality;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.air.MonitorStationAirQualityVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class MonitorStationAirQualityDaoImpl extends BaseDao implements MonitorStationAirQualityDao {
    @Override
    public ResultData insert(MonitorStationAirQuality monitorStationAirQuality) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.monitorair.insert", monitorStationAirQuality);
            result.setData(monitorStationAirQuality);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertBatch(List<MonitorStationAirQuality> list) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.monitorair.insertBatch", list);
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertLatest(MonitorStationAirQuality monitorStationAirQuality) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.monitorair.insertLatest", monitorStationAirQuality);
            result.setData(monitorStationAirQuality);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertLatestBatch(List<MonitorStationAirQuality> list) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.monitorair.insertLatestBatch", list);
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData selectLatest(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MonitorStationAirQualityVo> list = sqlSession.selectList("gmair.airquality.monitorair.selectLatest", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
