package com.example.tfhbackend.controller;

import com.example.tfhbackend.dto.OrderDTO;
import com.example.tfhbackend.dto.TableDTO;
import com.example.tfhbackend.dto.response.PaginatedResponse;
import com.example.tfhbackend.dto.response.Response;
import com.example.tfhbackend.service.OrderService;
import com.example.tfhbackend.service.TableService;
import com.example.tfhbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class TableController {
    private final TableService tableService;
    private final UserService userService;
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<TableDTO>> get(@RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(defaultValue = "10") int perPage) {
        log.info("/api/tables: GET request with parameters: page: {}, perPage: {}", page, perPage);
        var response = tableService.get(page, perPage);
        log.info("/api/tables: Response status: {}", HttpStatus.OK);
        return ResponseEntity.ok(
                new PaginatedResponse<>(response)
        );
    }

    @GetMapping("/waiter")
    public ResponseEntity<Response<List<TableDTO>>> getForWaiter() {
        return ResponseEntity.ok(
                new Response<>(tableService.getForWaiter(userService.getCurrentLoggedUser()))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<TableDTO>> getById(@PathVariable Long id) {
        log.info("/api/tables/{id}: GET request with parameters: id: {}", id);
        var response = tableService.getById(id);
        log.info("/api/tables/{id}: Response Status: {}", HttpStatus.OK);
        return ResponseEntity.ok(
                new Response<>(response)
        );
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<Response<List<OrderDTO>>> getOrdersForTable(@PathVariable Long id) {
        return ResponseEntity.ok(
                new Response<>(orderService.getForTable(id))
        );
    }

    @PostMapping
    public ResponseEntity<Response<TableDTO>> add(@RequestBody TableDTO table) {
        log.info("/api/tables: POST request for adding table with nrOFSpots: {}", table.getNrOfSpots());
        var response = tableService.add(table);
        log.info("/api/tables: Table added, id={}, Response status: {}", response.getId(), HttpStatus.CREATED);
        return new ResponseEntity<>(
                new Response<>(response),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<TableDTO>> update(@PathVariable Long id,
                                                     @RequestBody TableDTO table) {
        log.info("/api/tables/{id}: PUT request for updating table with id: {}", id);
        table.setId(id);
        TableDTO response = tableService.update(table);
        log.info("/api/tables/{id}: Table updated, Response status: {}", HttpStatus.OK);
        return ResponseEntity.ok(
                new Response<>(response)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        log.info("/api/tables/{id}: DELETE request for removing table with id: {}", id);
        tableService.remove(id);
        log.info("/api/tables/{id}: Table with id {} removed, Response status={}", id, HttpStatus.NO_CONTENT);
        return ResponseEntity.noContent().build();
    }
}
