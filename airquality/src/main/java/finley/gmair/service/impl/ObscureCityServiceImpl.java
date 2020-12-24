package finley.gmair.service.impl;

import finley.gmair.dao.ObscureCityDao;
import finley.gmair.model.air.ObscureCity;
import finley.gmair.service.ObscureCityService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class ObscureCityServiceImpl implements ObscureCityService{

    @Autowired
    ObscureCityDao obscureCityDao;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return obscureCityDao.query(condition);
    }

    @Override
    public ResultData assign(ObscureCity obscureCity) {
        return obscureCityDao.replace(obscureCity);
    }
}
