package com.goldengekko.lhr.web;
import com.goldengekko.lhr.service.BusinessError;
import com.goldengekko.lhr.util.CircuitBreaker.Breaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.goldengekko.docrest.domain.RestCodes;
import com.goldengekko.docrest.domain.RestReturn;
import com.goldengekko.lhr.domain.Flight;
import com.goldengekko.lhr.service.FlightInfoService;

@Controller
@RestReturn
@RequestMapping(value = "{domain}")
public class FlightController {
    private static final Logger LOG = LoggerFactory.getLogger(FlightController.class);
    private FlightInfoService flightInfoService;

    @RestReturn(value=Flight.class, entity=Flight.class, codes={@RestCodes(codes="200, 404=Requested flight could not be found., 500=Internal Server Error")})
    @RequestMapping(value = "/v10/flight/{id}", method = RequestMethod.GET)
    public ResponseEntity<Flight> findById(@PathVariable String id, @RequestParam(required = false) String hostScheduledDate,
                                           @RequestParam String arrivalOrDeparture)  {
        try {
            if (Breaker.Guard.thirdPartyToCircuit.get(Breaker.FI_HUB).isOpen()) {
                //to throw exception
                BusinessError.GEOLOCATION_NAME_ALREADY_EXISTED.throwExceptionWithPlaceHolder(Breaker.FI_HUB);
            }
            LOG.debug("arrivalOrDeparture: " + arrivalOrDeparture);
            Flight flight = flightInfoService.getFlightById(id, hostScheduledDate, arrivalOrDeparture);
            if (flight == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            LOG.debug("<<< Exiting method: getFlightById");
            return new ResponseEntity<>(flight, HttpStatus.OK);
        } catch (Exception ex) {
            Breaker.Guard.thirdPartyToCircuit.get(Breaker.FI_HUB).newException();
            throw ex;
        }
    }
}