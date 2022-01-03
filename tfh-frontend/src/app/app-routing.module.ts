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

const routes: Routes = [
  {path: 'dashboard', component: AdminDashboardComponent, canActivate: [AuthGuard], children: [
      {path: 'menu-items', component: MenuItemsListComponent, canActivate: [AuthGuard]},
      {path: 'tables', component: TablesListComponent, canActivate: [AuthGuard]},
      {path: 'feedback', component: FeedbackListComponent, canActivate: [AuthGuard]},
      {path: 'users', component: UsersListComponent, canActivate: [AuthGuard]},
      {path: 'diagnostics', component: AdminDiagnosticsComponent, canActivate: [AuthGuard]},
    ]},
  {path: '', component: LoginComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
