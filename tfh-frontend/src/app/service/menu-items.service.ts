import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {PaginatedResponseModel} from "../dto/paginated-response.model";
import {MenuItemModel} from "../model/menu-item-model";
import {ResponseModel} from "../dto/response.model";
import {FoodCategoryModel} from "../dto/food-category.model";
import {MenuItemPerCategoryDto} from "../dto/menu-item-per-category-dto";

@Injectable()
export class MenuItemsService {
  public url = 'http://localhost:8081/api/menu_items'

  constructor(public http: HttpClient) {
  }

  get(page: number = 1, perPage: number = 10) : Observable<PaginatedResponseModel<MenuItemModel>> {
    return this.http.get<PaginatedResponseModel<MenuItemModel>>(this.url, {
      params: new HttpParams().set('page', page).set('perPage', perPage)
    })
  }

  getAllPerCategories(): Observable<ResponseModel<MenuItemPerCategoryDto[]>> {
    return this.http.get<ResponseModel<MenuItemPerCategoryDto[]>>(this.url + "/all")
  }

  getById(id: number) {
    return this.http.get<ResponseModel<MenuItemModel>>(`${this.url}/${id}`);
  }

  add(item: FormData) : Observable<ResponseModel<MenuItemModel>> {
    return this.http.post<ResponseModel<MenuItemModel>>(this.url, item);
  }

  update(id: number, item: FormData) : Observable<ResponseModel<MenuItemModel>> {
    return this.http.patch<ResponseModel<MenuItemModel>>(`${this.url}/${id}`, item);
  }

  delete(id: number) : Observable<any> {
    return this.http.delete(`${this.url}/${id}`)
  }

  getFoodCategories(): Observable<ResponseModel<FoodCategoryModel[]>> {
    return this.http.get<ResponseModel<FoodCategoryModel[]>>('http://localhost:8081/api/food_categories');
  }

}
