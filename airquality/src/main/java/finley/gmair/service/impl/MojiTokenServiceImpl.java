package finley.gmair.service.impl;

import finley.gmair.dao.MojiTokenDao;
import finley.gmair.model.air.MojiToken;
import finley.gmair.service.MojiTokenService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MojiTokenServiceImpl implements MojiTokenService {

    @Autowired
    private MojiTokenDao mojiTokenDao;

    @Override
    public ResultData create(MojiToken mojiToken) {
        ResultData result = new ResultData();
        ResultData response = mojiTokenDao.insert(mojiToken);
        if(response.getResponseCode()!= ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("create moji token error");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = mojiTokenDao.query(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("query is null");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("query is error");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }
}
