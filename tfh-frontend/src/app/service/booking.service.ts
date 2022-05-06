import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ResponseModel} from "../dto/response.model";
import {BookingDtoModel} from "../dto/booking-dto.model";
import {BookingConfirmationModel} from "../dto/booking-confirmation.model";

@Injectable()
export class BookingService {
  private url = environment.api + 'bookings';

  constructor(private http: HttpClient) {
  }

  book(booking: BookingDtoModel) : Observable<ResponseModel<any>> {
    return this.http.post<ResponseModel<any>>(this.url, booking);
  }

  confirm(id: number, request: BookingConfirmationModel) {
    return this.http.post<ResponseModel<any>>(`${this.url}/${id}/confirm`, request);
  }
}
