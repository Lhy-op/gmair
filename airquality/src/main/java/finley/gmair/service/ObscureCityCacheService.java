package finley.gmair.service;

import finley.gmair.model.air.ObscureCity;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

@Component
public interface ObscureCityCacheService {

    @CachePut(value = "ObscureCityMap", key = "#obscureCity.cityName",
            condition = "#obscureCity != null", unless = "#obscureCity == null ")
    default ObscureCity generate(ObscureCity obscureCity) {
        return obscureCity;
    };
    ObscureCity fetch(String cityName);

}
