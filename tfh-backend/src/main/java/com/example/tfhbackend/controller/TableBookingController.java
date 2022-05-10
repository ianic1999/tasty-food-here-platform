package com.example.tfhbackend.controller;

import com.example.tfhbackend.dto.TableDTO;
import com.example.tfhbackend.dto.TimeRangeDTO;
import com.example.tfhbackend.dto.request.TableBookingRequest;
import com.example.tfhbackend.dto.response.Response;
import com.example.tfhbackend.service.TableBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TableBookingController {

    private final TableBookingService tableBookingService;

    @GetMapping("/free")
    public ResponseEntity<Response<List<TableDTO>>> getFreeTables(@RequestParam String date,
                                                                  @RequestParam String time,
                                                                  @RequestParam int duration) {
        TableBookingRequest request = new TableBookingRequest(date, time, duration);
        return ResponseEntity.ok(
                new Response<>(tableBookingService.getFreeTables(request))
        );
    }

    @GetMapping("/available")
    public ResponseEntity<Response<TimeRangeDTO>> getAvailableTables(@RequestParam String date,
                                                                    @RequestParam String time,
                                                                    @RequestParam int duration) {
        TableBookingRequest request = new TableBookingRequest(date, time, duration);
        return ResponseEntity.ok(
                new Response<>(tableBookingService.getFirstAvailableTable(request))
        );
    }
}
