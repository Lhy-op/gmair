package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ProvinceAirqualityDao;
import finley.gmair.model.air.ProvinceAirQuality;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class ProvinceAirqualityDaoImpl extends BaseDao implements ProvinceAirqualityDao {

    @Override
    public ResultData insert(ProvinceAirQuality provinceAirQuality) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.province.insert", provinceAirQuality);
            result.setData(provinceAirQuality);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertBatch(List<ProvinceAirQuality> provinceAirQualities) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.province.insertBatch", provinceAirQualities);
            result.setData(provinceAirQualities);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData select(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<ProvinceAirQuality> list = sqlSession.selectList("gmair.airquality.province.select", condition);
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
