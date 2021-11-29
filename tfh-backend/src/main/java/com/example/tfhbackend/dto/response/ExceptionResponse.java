package com.example.tfhbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionResponse {
    private String key;
    private Map<String, List<String>> messages;
}
