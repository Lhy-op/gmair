package finley.gmair.dao.impl;

import finley.gmair.dao.MonitorStationDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.air.MonitorStation;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.air.MonitorStationVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class MonitorStationDaoImpl extends BaseDao implements MonitorStationDao {

    @Override
    public ResultData insert(MonitorStation monitorStation) {
        ResultData result = new ResultData();
        monitorStation.setStationId(IDGenerator.generate("ms"));
        try {
            sqlSession.insert("gmair.airquality.monitorstation.replace", monitorStation);
            result.setData(monitorStation);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertBatch(List<MonitorStation> list) {
        ResultData result = new ResultData();
        for (MonitorStation monitorStation: list) {
            monitorStation.setStationId(IDGenerator.generate("ms"));
        }
        try {
            sqlSession.insert("gmair.airquality.monitorstation.replaceBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MonitorStationVo> list = sqlSession.selectList("gmair.airquality.monitorstation.select", condition);
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

    @Override
    public ResultData empty() {
        ResultData result = new ResultData();
        try {
            sqlSession.delete("gmair.airquality.monitorstation.empty");
        }catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
