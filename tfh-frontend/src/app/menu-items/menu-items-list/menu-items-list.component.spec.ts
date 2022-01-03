import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MenuItemsListComponent} from './menu-items-list.component';
import {Observable, of} from "rxjs";
import {PaginatedResponseModel} from "../../dto/paginated-response.model";
import {MenuItemModel} from "../../model/menu-item-model";
import {PaginationModel} from "../../dto/pagination.model";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {ResponseModel} from "../../dto/response.model";
import {FoodCategoryModel} from "../../dto/food-category.model";
import {NO_ERRORS_SCHEMA} from "@angular/core";
import {MenuItemsService} from "../../service/menu-items.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialog} from "@angular/material/dialog";
import {By} from "@angular/platform-browser";

class MockMenuItemService {
  public url = 'http://localhost:8081/api/menu_items';

  constructor(public http: HttpClient) {
  }

  get(page: number = 1, perPage: number = 10): Observable<PaginatedResponseModel<MenuItemModel>> {
    return of<PaginatedResponseModel<MenuItemModel>>(
      new PaginatedResponseModel<MenuItemModel>(
        [
          new MenuItemModel(1, 'item1', 'category', 100, 'image1'),
          new MenuItemModel(2, 'item2', 'category', 200, 'image2'),
        ],
        new PaginationModel(2, 2, 2, 1, {next: true, previous: false})
      ));
  }

  getById(id: number): Observable<ResponseModel<MenuItemModel>> {
    return Observable.create(
      new MenuItemModel(1, 'item1', 'category', 100, 'image1')
    );
  }

  add(item: FormData): Observable<ResponseModel<MenuItemModel>> {
    return Observable.create(
      new MenuItemModel(3, 'item3', 'category', 300, 'image3')
    );
  }

  update(id: number, item: FormData): Observable<ResponseModel<MenuItemModel>> {
    return Observable.create(
      new MenuItemModel(4, 'item4', 'category', 400, 'image4')
    );
  }

  delete(id: number): Observable<any> {
    return Observable.create();
  }

  getFoodCategories(): Observable<ResponseModel<FoodCategoryModel[]>> {
    return Observable.create(
      new FoodCategoryModel("cat1", "category 1"),
      new FoodCategoryModel("cat2", "category 2"),
    );
  }
}

describe('MenuItemsListComponent', () => {
  let component: MenuItemsListComponent;
  let fixture: ComponentFixture<MenuItemsListComponent>;
  let service: MockMenuItemService;
  let httpClientSpy = jasmine.createSpyObj('HttpClient', ['post', 'get', 'patch', 'delete']);
  let snackBarSpy = jasmine.createSpyObj('MatSnackBar', ['open']);
  let dialogSpy = jasmine.createSpyObj('MatDialog', ['open']);
  let openDialogSpy = jasmine.createSpy('open');

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      schemas: [NO_ERRORS_SCHEMA],
      declarations: [MenuItemsListComponent],
      providers: [
        {
        provide: MenuItemsService, useValue: new MockMenuItemService(httpClientSpy)
      },
        {
          provide: MatSnackBar, useValue: snackBarSpy
        },
        {
          provide: MatDialog, useValue: dialogSpy
        },
      ],
      imports: [HttpClientModule]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MenuItemsListComponent);
    component = fixture.componentInstance;
    component.page = 1;
    component.totalPages = 3;
    component.previous = false;
    component.next = true;
    component.dataSource = [
      new MenuItemModel(1, 'item1', 'category', 100, 'image1'),
      new MenuItemModel(2, 'item2', 'category', 200, 'image2'),
    ];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have current page 1 and total 3', () => {
    let text = fixture.debugElement.query(By.css('#page')).nativeElement.textContent;
    expect(text).toBe('Page 1 of 3')
  });

  it('should have previous disabled', () => {
    let button = fixture.debugElement.nativeElement.querySelector('#left-arrow');
    expect(button.disabled).toBeTruthy();
  });

  it('should have next enabled', () => {
    let button = fixture.debugElement.nativeElement.querySelector('#right-arrow');
    expect(button.disabled).toBeFalsy();
  });

  it('should change page when next clicked', () => {
    let button = fixture.debugElement.nativeElement.querySelector('#right-arrow');
    button.click();
    fixture.detectChanges();
    let text = fixture.debugElement.query(By.css('#page')).nativeElement.textContent;
    expect(text).toBe('Page 2 of 3')
  });

  it('should disable next when last page', () => {
    let button = fixture.debugElement.nativeElement.querySelector('#right-arrow');
    component.next = false;
    fixture.detectChanges();
    expect(button.disabled).toBeTrue();
  });

  it('should open dialog when add button clicked', () => {
    let button = fixture.debugElement.nativeElement.querySelector('#add-button');
    button.click();
    expect(dialogSpy.open).toHaveBeenCalled();
  });

});
