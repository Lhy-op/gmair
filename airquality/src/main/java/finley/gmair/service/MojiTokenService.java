package finley.gmair.service;

import finley.gmair.model.air.MojiToken;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MojiTokenService {

    ResultData create(MojiToken mojiToken);

    ResultData fetch(Map<String,Object> condition);
}
