import {AfterViewInit, Component, OnInit} from '@angular/core';
import {MenuItemModel} from "../../model/menu-item-model";
import {MenuItemsService} from "../../service/menu-items.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialog} from "@angular/material/dialog";
import {MenuItemsAddComponent} from "../menu-items-add/menu-items-add.component";

@Component({
  selector: 'app-menu-items-list',
  templateUrl: './menu-items-list.component.html',
  styleUrls: ['./menu-items-list.component.css']
})
export class MenuItemsListComponent implements OnInit, AfterViewInit {

  constructor(private menuItemService: MenuItemsService,
              private snackBar: MatSnackBar,
              private dialog: MatDialog) { }

  columns=['name', 'category', 'price', 'image', 'actions'];
  dataSource : MenuItemModel[] = [];
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
    this.menuItemService.get(this.page).toPromise().then(
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
    this.menuItemService.delete(id)
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

  openAddDialog() {
    const dialogRef = this.dialog.open(
      MenuItemsAddComponent, {
        data: new FormData()
      }
    );

    dialogRef.afterClosed().subscribe(response => {
      if (response != null) {
        this.menuItemService.add(response).toPromise()
          .then(_ => this.updatePage())
          .catch(error => console.log(error));
      }
    })
  }
}
