import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {OrderModel} from "../dto/order.model";
import {Observable} from "rxjs";

@Injectable()
export class OrderService {
  private url = environment.api + 'orders';

  constructor(private http: HttpClient) {
  }

  public add(request: OrderModel) : Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(this.url, request)
  }
}
