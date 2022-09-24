package com.asphyxia.locationSender;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/map")
public class Controller {

    @Autowired
    LocationService locationService;

    @RequestMapping("/{id}")
    public void postMessage(@PathVariable("id") int id) throws IOException {
        locationService.postMessage(id);
    }
}
