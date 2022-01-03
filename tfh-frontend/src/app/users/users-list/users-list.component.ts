import { Component, OnInit } from '@angular/core';
import {MenuItemsService} from "../../service/menu-items.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialog} from "@angular/material/dialog";
import {MenuItemModel} from "../../model/menu-item-model";
import {MenuItemsAddComponent} from "../../menu-items/menu-items-add/menu-items-add.component";
import {UsersService} from "../../service/users.service";
import {UserModel} from "../../dto/user.model";

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.css']
})
export class UsersListComponent implements OnInit {

  constructor(private userService: UsersService,
              private snackBar: MatSnackBar,
              private dialog: MatDialog) { }

  columns=['firstName', 'lastName', 'phone', 'email', 'image', 'activated', 'actions'];
  dataSource : UserModel[] = [];
  page = 1;
  totalPages = 5;
  previous = false;
  next = true;

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    this.updatePage();
  }

  updatePage() {
    this.userService.get(this.page).toPromise().then(
      response => {
        this.dataSource = response!.data;
        this.page = response!.pagination!.currentPage;
        this.totalPages = response!.pagination!.total;
        this.next = response!.pagination!.links.next;
        this.previous = response!.pagination!.links.previous;
      }
    )
      .catch(_ => {
        this.snackBar.open('Menu items not loaded!', 'OK', {duration: 3000});
      })
  }

  delete(id: number) {
    this.userService.delete(id)
      .toPromise()
      .then(_ => {
        this.updatePage()
      })
      .catch(error => console.log(error))
  }

  previousPage() {
    this.page -= 1;
    this.updatePage();
  }

  nextPage() {
    this.page += 1;
    this.updatePage();
  }

}
