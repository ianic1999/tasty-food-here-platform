import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {PaginatedResponseModel} from "../dto/paginated-response.model";
import {ResponseModel} from "../dto/response.model";
import {TableModel} from "../dto/table.model";

@Injectable()
export class TablesService {
  private url = 'http://104.248.205.251:8081/api/tables'

  constructor(private http: HttpClient) {
  }

  get(page: number = 1, perPage: number = 10) : Observable<PaginatedResponseModel<TableModel>> {
    return this.http.get<PaginatedResponseModel<TableModel>>(this.url, {
      params: new HttpParams().set('page', page).set('perPage', perPage)
    })
  }

  getById(id: number) {
    return this.http.get<ResponseModel<TableModel>>(`${this.url}/${id}`);
  }

  add(item: TableModel) : Observable<ResponseModel<TableModel>> {
    return this.http.post<ResponseModel<TableModel>>(this.url, item);
  }

  update(id: number, item: TableModel) : Observable<ResponseModel<TableModel>> {
    return this.http.put<ResponseModel<TableModel>>(`${this.url}/${id}`, item);
  }

  delete(id: number) : Observable<any> {
    return this.http.delete(`${this.url}/${id}`)
  }
}
