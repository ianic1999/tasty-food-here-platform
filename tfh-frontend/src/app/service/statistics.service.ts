import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ResponseModel} from "../dto/response.model";
import {StatisticsModel} from "../dto/statistics.model";
import {ItemsPerCategoryStatisticsModel} from "../dto/items-per-category-statistics.model";
import {BookingsPerDayModel} from "../dto/bookings-per-day.model";
import {BookingsPerWeekModel} from "../dto/bookings-per-week.model";
import {BookingsPerMonthModel} from "../dto/bookings-per-month.model";


@Injectable()
export class StatisticsService {
  private url = 'http://localhost:8081/api/statistics';

  constructor(private http: HttpClient) {
  }

  getStatistics() : Observable<ResponseModel<StatisticsModel>> {
    return this.http.get<ResponseModel<StatisticsModel>>(this.url);
  }

  getItemsPerCategoriesStatistics() : Observable<ResponseModel<ItemsPerCategoryStatisticsModel[]>> {
    return this.http.get<ResponseModel<ItemsPerCategoryStatisticsModel[]>>(`${this.url}/items_per_categories`)
  }

  getBookingsPerDayStatistics(): Observable<ResponseModel<BookingsPerDayModel[]>> {
    return this.http.get<ResponseModel<BookingsPerDayModel[]>>(`${this.url}/bookings_per_day`)
  }

  getBookingsPerWeekStatistics(): Observable<ResponseModel<BookingsPerWeekModel[]>> {
    return this.http.get<ResponseModel<BookingsPerWeekModel[]>>(`${this.url}/bookings_per_week`)
  }

  getBookingsPerMonthStatistics(): Observable<ResponseModel<BookingsPerMonthModel[]>> {
    return this.http.get<ResponseModel<BookingsPerMonthModel[]>>(`${this.url}/bookings_per_month`)
  }
}
