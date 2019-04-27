package com.sherlockcodes.ubitricity.controllers;

import com.sherlockcodes.ubitricity.service.StationService;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/station")
@Api(value = "manage station", description = "basic controller that lets users manage stations")

public class StationController {

    private static final Logger logger = LogManager.getLogger(StationController.class);
    @Autowired
    StationService generatorService;

    @PostMapping("/{number}")
    @ApiOperation(value = "plug in a vehicle at station number given in the path")
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Server Exception")
    })
    public ResponseEntity<Object> addCar(@PathVariable @ApiParam("enter a value between 1 to 10") int number) throws Exception {
        if (number < 1 || number > 10)
           throw new Exception();
        generatorService.add(number);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{number}")
    @ApiOperation(value = "unplug a vehicle at station number given in the path")
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Server Exception")
    })
    public void removeCar(@PathVariable @ApiParam("enter a value between 1 to 10") int number) throws Exception {
        if (number < 1 || number > 10)
            throw new Exception();
        generatorService.delete(number);

    }

    @GetMapping("/status")
    @ApiOperation(value = "get the status")
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Server Exception")
    })
    public ResponseEntity getStatus() {
        return ResponseEntity.ok(generatorService.getStatus());
    }

}



