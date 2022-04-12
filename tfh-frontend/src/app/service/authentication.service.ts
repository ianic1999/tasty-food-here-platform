import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, map} from "rxjs";
import {ResponseModel} from "../dto/response.model";
import {JwtModel} from "../dto/jwt.model";

@Injectable()
export class AuthenticationService {

  private url = 'http://104.248.205.251:8081/api/auth/login';

  constructor(private http: HttpClient) {
  }

  login(phone: string, password: string) {
    return this.http.post<ResponseModel<JwtModel>>(this.url, {phone: phone, password: password})
      .pipe(map(response => {
        if (response.data.accessToken) {
          window.sessionStorage.removeItem('currentUser');
          window.sessionStorage.setItem('currentUser', response.data.accessToken);
        }
        return response.data;
      }))
  }

  logout() {
    localStorage.removeItem('currentUser');
    window.sessionStorage.clear();
  }

  currentUserValue() {
    return window.sessionStorage.getItem('currentUser');
  }
}
