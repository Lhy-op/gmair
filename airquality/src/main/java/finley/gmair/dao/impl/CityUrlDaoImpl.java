package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.CityUrlDao;
import finley.gmair.model.air.CityUrl;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.air.CityUrlVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class CityUrlDaoImpl extends BaseDao implements CityUrlDao {

    @Override
    public ResultData select(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<CityUrlVo> cityUrlVoList = sqlSession.selectList("gmair.airquality.cityUrl.select", condition);
            if (cityUrlVoList.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(cityUrlVoList);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }

        return null;
    }

    @Override
    public ResultData replace(CityUrl cityUrl) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.cityUrl.replace", cityUrl);
            result.setData(cityUrl);
            return result;
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResultData replaceBatch(List<CityUrl> cityUrlList) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.cityUrl.replaceBatch", cityUrlList);
            result.setData(cityUrlList);
            return result;
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
}
