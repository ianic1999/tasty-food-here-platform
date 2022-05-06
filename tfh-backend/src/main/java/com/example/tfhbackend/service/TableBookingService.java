package com.example.tfhbackend.service;

import com.example.tfhbackend.dto.TableDTO;
import com.example.tfhbackend.dto.request.TableBookingRequest;

import java.util.List;

public interface TableBookingService {
    List<TableDTO> getFreeTables(TableBookingRequest request);
}
