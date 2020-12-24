package finley.gmair.service.feign;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("location-agent")
public interface LocationFeign {
    @RequestMapping(value = "/location/address/resolve", method = RequestMethod.POST)
    ResultData geocoder(@RequestParam("address") String address);

    @RequestMapping(value = "/location/province/list", method = RequestMethod.GET)
    ResultData province();

    @RequestMapping(value = "/location/{provinceId}/cities", method = RequestMethod.GET)
    ResultData city(@PathVariable("provinceId") String provinceId);

    @GetMapping("/location/{cityId}/districts")
    ResultData district(@PathVariable("cityId") String cityId);

    @RequestMapping(value = "/location/probe/provinceId", method = RequestMethod.GET)
    ResultData detail(@RequestParam("cityId") String cityId);
}
