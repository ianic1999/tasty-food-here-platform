package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.TableDTO;
import com.example.tfhbackend.mapper.Mapper;
import com.example.tfhbackend.model.Table;
import com.example.tfhbackend.model.exception.EntityNotFoundException;
import com.example.tfhbackend.repository.TableRepository;
import com.example.tfhbackend.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
class TableServiceImpl implements TableService {
    private final TableRepository tableRepository;
    private final Mapper<Table, TableDTO> mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<TableDTO> get(int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        return tableRepository.findAll(pageable).map(mapper::map);
    }

    @Override
    @Transactional(readOnly = true)
    public TableDTO getById(Long id) {
        return mapper.map(findTableById(id));
    }

    @Override
    @Transactional
    public TableDTO add(TableDTO dto) {
        Table table = Table.builder()
                .nrOfSpots(dto.getNrOfSpots())
                .bookings(Collections.emptyList())
                .build();
        table = tableRepository.save(table);
        table.setOrdinalNumber(table.getId().intValue() + 1000);
        return mapper.map(table);
    }

    @Override
    @Transactional
    public TableDTO update(TableDTO dto) {
        Table table = findTableById(dto.getId());
        table.setNrOfSpots(dto.getNrOfSpots());
        return mapper.map(table);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        tableRepository.deleteById(id);
    }

    private Table findTableById(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Table with id " + id + " not found"));
    }
}
