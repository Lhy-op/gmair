package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ObscureCityDao;
import finley.gmair.model.air.ObscureCity;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ObscureCityDaoImpl extends BaseDao implements ObscureCityDao{

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<ObscureCity> list = sqlSession.selectList("gmair.airquality.obscurecity.select", condition);
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

    @Override
    public ResultData replace(ObscureCity obscureCity) {
        ResultData result = new ResultData();
        if (obscureCity.getOcId() == null) {
            obscureCity.setOcId(IDGenerator.generate("OCI"));
        }
        try {
            sqlSession.insert("gmair.airquality.obscurecity.replace", obscureCity);
            result.setData(obscureCity);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
