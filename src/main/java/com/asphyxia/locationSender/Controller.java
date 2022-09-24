package com.asphyxia.locationSender;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
public class Controller {

    @Autowired
    LocationService locationService;

    @RequestMapping("/{id}")
    public void postMessage(@PathVariable("id") int id) throws JsonProcessingException {
        locationService.postMessage(id);
    }
}
