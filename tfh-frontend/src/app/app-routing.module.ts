import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AdminDashboardComponent} from "./admin/admin-dashboard/admin-dashboard.component";
import {MenuItemsListComponent} from "./menu-items/menu-items-list/menu-items-list.component";
import {TablesListComponent} from "./tables/tables-list/tables-list.component";
import {FeedbackListComponent} from "./feedback/feedback-list/feedback-list.component";
import {UsersListComponent} from "./users/users-list/users-list.component";
import {AuthGuard} from "./auth/auth.guard";
import {LoginComponent} from "./login/login.component";
import {AdminDiagnosticsComponent} from "./admin/admin-diagnostics/admin-diagnostics.component";
import {HomeComponent} from "./home/home.component";
import {MenuComponent} from "./menu/menu.component";
import {ClientFeedbackComponent} from "./client-feedback/client-feedback.component";
import {BookingComponent} from "./booking/booking.component";
import {BookingTablesComponent} from "./booking-tables/booking-tables.component";

const routes: Routes = [
  {path: 'dashboard', component: AdminDashboardComponent, canActivate: [AuthGuard], children: [
      {path: 'menu-items', component: MenuItemsListComponent, canActivate: [AuthGuard]},
      {path: 'tables', component: TablesListComponent, canActivate: [AuthGuard]},
      {path: 'feedback', component: FeedbackListComponent, canActivate: [AuthGuard]},
      {path: 'users', component: UsersListComponent, canActivate: [AuthGuard]},
      {path: 'diagnostics', component: AdminDiagnosticsComponent, canActivate: [AuthGuard]},
    ]},
  {path: 'home', component: HomeComponent, children: [
      {path: 'menu', component: MenuComponent},
      {path: 'feedback', component: ClientFeedbackComponent},
      {path: 'bookings', component: BookingComponent, children: [
          {path: 'tables', component: BookingTablesComponent}
        ]},
    ]},
  {path: '', component: HomeComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
