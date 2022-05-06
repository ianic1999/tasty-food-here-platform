import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AdminDashboardComponent} from './admin/admin-dashboard/admin-dashboard.component';
import {AdminNavbarComponent} from './admin/admin-navbar/admin-navbar.component';
import {AdminDashboardContentComponent} from './admin/admin-dashboard-content/admin-dashboard-content.component';
import {MenuItemsListComponent} from './menu-items/menu-items-list/menu-items-list.component';
import {MatListModule} from "@angular/material/list";
import {MatIconModule} from "@angular/material/icon";
import {MatTableModule} from "@angular/material/table";
import {MatButtonModule} from "@angular/material/button";
import {MenuItemsService} from "./service/menu-items.service";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MenuItemsAddComponent} from './menu-items/menu-items-add/menu-items-add.component';
import {MatCardModule} from "@angular/material/card";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {MatDialogModule} from "@angular/material/dialog";
import {NgxMatFileInputModule} from "@angular-material-components/file-input";
import {FormsModule} from "@angular/forms";
import {TablesListComponent} from './tables/tables-list/tables-list.component';
import {TablesService} from "./service/tables.service";
import {TablesAddComponent} from './tables/tables-add/tables-add.component';
import {FeedbackListComponent} from './feedback/feedback-list/feedback-list.component';
import {FeedbackService} from "./service/feedback.service";
import {UsersListComponent} from './users/users-list/users-list.component';
import {UsersService} from "./service/users.service";
import {LoginComponent} from './login/login.component';
import {MatToolbarModule} from "@angular/material/toolbar";
import {AuthenticationService} from "./service/authentication.service";
import {JwtInterceptor} from "./auth/jwt.interceptor";
import {ErrorInterceptor} from "./auth/error.interceptor";
import {AdminDiagnosticsComponent} from './admin/admin-diagnostics/admin-diagnostics.component';
import {ActuatorService} from "./service/actuator.service";
import {StatisticsService} from "./service/statistics.service";
import {NgApexchartsModule} from "ng-apexcharts";
import { TablesEditComponent } from './tables/tables-edit/tables-edit.component';
import { NavigationBarComponent } from './navigation-bar/navigation-bar.component';
import { HomeComponent } from './home/home.component';
import {MatTabsModule} from "@angular/material/tabs";
import { MenuComponent } from './menu/menu.component';
import { ClientFeedbackComponent } from './client-feedback/client-feedback.component';
import { BookingComponent } from './booking/booking.component';
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import {TableBookingService} from "./service/table-booking.service";
import {DatePipe} from "@angular/common";
import { BookingTablesComponent } from './booking-tables/booking-tables.component';
import { BookingConfirmComponent } from './booking-confirm/booking-confirm.component';
import {BookingService} from "./service/booking.service";
import {NgxStarRatingModule} from "ngx-star-rating";

@NgModule({
  declarations: [
    AppComponent,
    AdminDashboardComponent,
    AdminNavbarComponent,
    AdminDashboardContentComponent,
    MenuItemsListComponent,
    MenuItemsAddComponent,
    TablesListComponent,
    TablesAddComponent,
    FeedbackListComponent,
    UsersListComponent,
    LoginComponent,
    AdminDiagnosticsComponent,
    TablesEditComponent,
    NavigationBarComponent,
    HomeComponent,
    MenuComponent,
    ClientFeedbackComponent,
    BookingComponent,
    BookingTablesComponent,
    BookingConfirmComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatListModule,
    MatIconModule,
    MatTableModule,
    MatButtonModule,
    HttpClientModule,
    MatSnackBarModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDialogModule,
    NgxMatFileInputModule,
    FormsModule,
    MatToolbarModule,
    NgApexchartsModule,
    MatTabsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    NgxStarRatingModule
  ],
  providers: [DatePipe, MenuItemsService, TablesService, FeedbackService, BookingService,
    UsersService, AuthenticationService, ActuatorService, StatisticsService, TableBookingService,
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
