package finley.gmair.dao;

import finley.gmair.model.air.MojiToken;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MojiTokenDao {

    ResultData insert(MojiToken mojiToken);

    ResultData query(Map<String,Object> condition);
}
