package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.util.ReflectionsUtil;
import com.example.tfhbackend.dto.TableDTO;
import com.example.tfhbackend.mapper.Mapper;
import com.example.tfhbackend.model.Table;
import com.example.tfhbackend.model.exception.EntityNotFoundException;
import com.example.tfhbackend.model.fixture.TableFixture;
import com.example.tfhbackend.repository.TableRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TableServiceImplTest {

    @Mock
    private TableRepository tableRepository;
    @Mock
    private Mapper<Table, TableDTO> mapper;
    @Captor
    private ArgumentCaptor<Pageable> pageCaptor;

    private TableServiceImpl tableService;

    private Table table;
    private TableDTO dto;
    private final int page = 1;
    private final int perPage = 10;

    @Before
    public void setup() {
        tableService = new TableServiceImpl(
                tableRepository,
                mapper
        );

        table = TableFixture.aTable();
        ReflectionsUtil.setId(table, 1L);
        dto = new TableDTO(
                1L,
                10001,
                10,
                1L
        );

        when(mapper.map(table))
                .thenReturn(dto);
        when(tableRepository.findAll(pageCaptor.capture()))
                .thenReturn(new PageImpl<>(List.of(table)));
        when(tableRepository.findById(1L))
                .thenReturn(Optional.of(table));
        when(tableRepository.save(any(Table.class)))
                .thenReturn(table);
    }

    @Test
    public void get_whenInvoked_correctPage() {
        tableService.get(page, perPage);
        var tablePage = pageCaptor.getValue();

        assertThat(tablePage.getPageSize()).isEqualTo(perPage);
        assertThat(tablePage.getPageNumber()).isEqualTo(page - 1);
    }

    @Test
    public void get_whenInvoked_correctResult() {
        var tablePage = tableService.get(page, perPage);

        assertThat(tablePage.getContent())
                .hasSize(1)
                .containsExactly(dto);
    }

    @Test
    public void getById_whenInvoked_correctResult() {
        var result = tableService.getById(1L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    public void getById_whenNotFound_exceptionThrown() {
        when(tableRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.getById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Table with id 1 not found");
    }

    @Test
    public void add_whenInvoked_correctOrdinalNumber() {
        table.setOrdinalNumber(1);

        tableService.add(dto);

        assertThat(table.getOrdinalNumber()).isEqualTo(1001);
    }

    @Test
    public void add_whenInvoked_correctResult() {
        var result = tableService.add(dto);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    public void update_whenInvoked_correctResult() {
        dto.setNrOfSpots(20);
        tableService.update(dto);

        assertThat(table.getNrOfSpots()).isEqualTo(20);
    }

    @Test
    public void remove_whenInvoked_callsRepo() {
        tableService.remove(1L);

        verify(tableRepository).deleteById(1L);
    }

}