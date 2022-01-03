import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {AuthenticationService} from "../service/authentication.service";
import {catchError, Observable, throwError} from "rxjs";


@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private authenticationService: AuthenticationService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req)
      .pipe(catchError(err => {
        if (err.status === 401) {
          this.authenticationService.logout();
          location.reload()
        }
        console.log(err)
        const error: string = err.error.messages.no_field[0] || err.statusText;
        return throwError(() => new Error(error));
      }))
  }
}
