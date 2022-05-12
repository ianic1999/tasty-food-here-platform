import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient, HttpParams} from "@angular/common/http";
import {TableBookingModel} from "../dto/table-booking.model";
import {Observable} from "rxjs";
import {ResponseModel} from "../dto/response.model";
import {TableModel} from "../dto/table.model";

@Injectable()
export class TableBookingService {
  private url = environment.api + 'tables';

  constructor(private http: HttpClient) {

  }

  getFreeTables(request: TableBookingModel): Observable<ResponseModel<TableModel[]>> {
    return this.http.get<ResponseModel<TableModel[]>>(this.url + "/free", {
      params: new HttpParams().set("date", request.date).set("time", request.time).set("duration", request.duration)
    });
  }

  getFirstAvailable(request: TableBookingModel): Observable<ResponseModel<any>> {
    return this.http.get<ResponseModel<any>>(this.url + "/available", {
      params: new HttpParams().set("date", request.date).set("time", request.time).set("duration", request.duration)
    });
  }
}
