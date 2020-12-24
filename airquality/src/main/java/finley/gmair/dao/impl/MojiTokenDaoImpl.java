package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MojiTokenDao;
import finley.gmair.model.air.MojiToken;
import finley.gmair.model.drift.DriftAddress;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MojiTokenDaoImpl extends BaseDao implements MojiTokenDao {

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MojiToken> list = sqlSession.selectList("gmair.airquality.mojitoken.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insert(MojiToken mojiToken) {
        ResultData result = new ResultData();
        mojiToken.setTokenId(IDGenerator.generate("GAT"));
        try {
            sqlSession.insert("gmair.airquality.mojitoken.insert", mojiToken);
            result.setData(mojiToken);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
