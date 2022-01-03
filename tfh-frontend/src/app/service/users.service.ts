import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {PaginatedResponseModel} from "../dto/paginated-response.model";
import {ResponseModel} from "../dto/response.model";
import {UserModel} from "../dto/user.model";

@Injectable()
export class UsersService {
  private url = 'http://localhost:8081/api/users'

  constructor(private http: HttpClient) {
  }

  get(page: number = 1, perPage: number = 10) : Observable<PaginatedResponseModel<UserModel>> {
    return this.http.get<PaginatedResponseModel<UserModel>>(this.url, {
      params: new HttpParams().set('page', page).set('perPage', perPage)
    })
  }

  getById(id: number) {
    return this.http.get<ResponseModel<UserModel>>(`${this.url}/${id}`);
  }

  add(item: FormData) : Observable<ResponseModel<UserModel>> {
    return this.http.post<ResponseModel<UserModel>>(this.url, item);
  }

  update(id: number, item: FormData) : Observable<ResponseModel<UserModel>> {
    return this.http.patch<ResponseModel<UserModel>>(`${this.url}/${id}`, item);
  }

  delete(id: number) : Observable<any> {
    return this.http.delete(`${this.url}/${id}`)
  }

  activate(id: number): Observable<any> {
    return this.http.post(`${this.url}/${id}/activate`, {userId: id});
  }
}
