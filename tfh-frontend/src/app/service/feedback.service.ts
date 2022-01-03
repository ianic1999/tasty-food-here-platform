import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {PaginatedResponseModel} from "../dto/paginated-response.model";
import {ResponseModel} from "../dto/response.model";
import {FeedbackModel} from "../dto/feedback.model";

@Injectable()
export class FeedbackService {
  private url = 'http://localhost:8081/api/feedbacks'

  constructor(private http: HttpClient) {
  }

  get(page: number = 1, perPage: number = 10) : Observable<PaginatedResponseModel<FeedbackModel>> {
    return this.http.get<PaginatedResponseModel<FeedbackModel>>(this.url, {
      params: new HttpParams().set('page', page).set('perPage', perPage)
    })
  }

  getById(id: number) {
    return this.http.get<ResponseModel<FeedbackModel>>(`${this.url}/${id}`);
  }

  add(item: FeedbackModel) : Observable<ResponseModel<FeedbackModel>> {
    return this.http.post<ResponseModel<FeedbackModel>>(this.url, item);
  }

  update(id: number, item: FeedbackModel) : Observable<ResponseModel<FeedbackModel>> {
    return this.http.patch<ResponseModel<FeedbackModel>>(`${this.url}/${id}`, item);
  }

  delete(id: number) : Observable<any> {
    return this.http.delete(`${this.url}/${id}`)
  }
}
