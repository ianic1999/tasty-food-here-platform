package com.example.tfhbackend.service.impl;

import com.example.tfhbackend.dto.MenuItemDTO;
import com.example.tfhbackend.mapper.Mapper;
import com.example.tfhbackend.model.MenuItem;
import com.example.tfhbackend.model.enums.FoodCategory;
import com.example.tfhbackend.model.exception.EntityNotFoundException;
import com.example.tfhbackend.model.fixture.MenuItemFixture;
import com.example.tfhbackend.repository.MenuItemRepository;
import com.example.tfhbackend.util.ImageHandler;
import com.example.tfhbackend.util.ReflectionsUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MenuItemServiceImplTest {

    @Mock
    private MenuItemRepository menuItemRepository;
    @Mock
    private ImageHandler imageHandler;
    @Mock
    private Mapper<MenuItem, MenuItemDTO> mapper;
    @Mock
    private MultipartFile file;
    @Captor
    private ArgumentCaptor<Pageable> pageCaptor;
    @Captor
    private ArgumentCaptor<MenuItem> menuItemCaptor;

    private MenuItemServiceImpl menuItemService;

    private MenuItem cola;
    private MenuItem pizza;
    private MenuItemDTO pizzaDto;
    private MenuItemDTO colaDto;
    private final String fileName = "filename.png";
    private final int page = 1;
    private final int perPage = 10;

    @Before
    public void setup() throws IOException {
        menuItemService = new MenuItemServiceImpl(
                menuItemRepository,
                imageHandler,
                mapper
        );

        cola = MenuItemFixture.cocaCola();
        pizza = MenuItemFixture.pizza();
        ReflectionsUtil.setId(cola, 1L);
        ReflectionsUtil.setId(pizza, 2L);
        pizzaDto = new MenuItemDTO(
                1L,
                "pizza",
                100.0,
                "image.png",
                "FAST_FOOD"
        );
        colaDto = new MenuItemDTO(
                2L,
                "cola",
                20.0,
                "image2.png",
                "BEVERAGE"
        );

        when(file.getOriginalFilename()).thenReturn(fileName);
        when(menuItemRepository.findAll(pageCaptor.capture())).thenReturn(new PageImpl<>(List.of(cola, pizza)));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(pizza));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(pizza);
        when(mapper.map(pizza)).thenReturn(pizzaDto);
        when(mapper.map(cola)).thenReturn(colaDto);
    }

    @Test
    public void get_whenInvoked_correctPage() {
        menuItemService.get(page, perPage);
        var pageable = pageCaptor.getValue();

        assertThat(pageable.getPageNumber()).isEqualTo(page - 1);
        assertThat(pageable.getPageSize()).isEqualTo(perPage);
    }

    @Test
    public void get_whenInvoked_correctResult() {
        var itemsPage = menuItemService.get(page, perPage);

        assertThat(itemsPage.getContent())
                .hasSize(2)
                .containsExactly(colaDto, pizzaDto);
    }

    @Test
    public void getById_whenNotFound_exceptionThrown() {
        when(menuItemRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuItemService.getById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Menu item with id 1 not found");
    }

    @Test
    public void getById_whenInvoked_correctResult() {
        var result = menuItemService.getById(1L);

        assertThat(result).isEqualTo(pizzaDto);
    }

    @Test
    public void add_whenInvoked_callsImageHandler() throws IOException {
        menuItemService.add(
                "fanta",
                25.0,
                FoodCategory.BEVERAGE.name(),
                file
        );

        verify(imageHandler).save(file, "menu", "2.png");
    }

    @Test
    public void add_whenInvoked_correctEntitySaved() throws IOException {
        menuItemService.add(
                "fanta",
                25.0,
                FoodCategory.BEVERAGE.name(),
                file
        );

        verify(menuItemRepository).save(menuItemCaptor.capture());
        var item = menuItemCaptor.getValue();
        assertThat(item.getName()).isEqualTo("fanta");
        assertThat(item.getPrice()).isEqualTo(25.0);
        assertThat(item.getCategory()).isEqualTo(FoodCategory.BEVERAGE);
    }

    @Test
    public void update_whenInvokedWithFile_removeOldImage() throws IOException {
        menuItemService.update(
                1L,
                "pizza_updated",
                30.0,
                null,
                file
        );

        verify(imageHandler).delete("pizza.png");
    }

    @Test
    public void update_whenNotFound_exceptionThrown() {
        when(menuItemRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuItemService.update(1L, null, null, null, null))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Menu item with id 1 not found");
    }

    @Test
    public void update_whenInvokedWithFile_saveNewImage() throws IOException {
        menuItemService.update(
                1L,
                "pizza_updated",
                30.0,
                null,
                file
        );

        verify(imageHandler).save(file, "menu", "2.png");
    }

    @Test
    public void update_whenInvoked_updateData() throws IOException {
        menuItemService.update(
                1L,
                "pizza_updated",
                30.0,
                null,
                file
        );

        assertThat(pizza.getName()).isEqualTo("pizza_updated");
        assertThat(pizza.getPrice()).isEqualTo(30.0);
        assertThat(pizza.getCategory()).isEqualTo(FoodCategory.FAST_FOOD);
    }

    @Test
    public void remove_whenInvoked_removesImage() throws IOException {
        menuItemService.remove(1L);

        verify(imageHandler).delete(pizza.getImage());
    }

    @Test
    public void remove_whenInvoked_callsRepo() throws IOException {
        menuItemService.remove(1L);

        verify(menuItemRepository).deleteById(1L);
    }

}