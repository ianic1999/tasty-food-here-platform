import { Component, OnInit } from '@angular/core';
import {MenuItemsService} from "../../service/menu-items.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialog} from "@angular/material/dialog";
import {MenuItemModel} from "../../model/menu-item-model";
import {MenuItemsAddComponent} from "../../menu-items/menu-items-add/menu-items-add.component";
import {TableModel} from "../../dto/table.model";
import {TablesService} from "../../service/tables.service";
import {TablesAddComponent} from "../tables-add/tables-add.component";
import {TablesEditComponent} from "../tables-edit/tables-edit.component";

@Component({
  selector: 'app-tables-list',
  templateUrl: './tables-list.component.html',
  styleUrls: ['./tables-list.component.css']
})
export class TablesListComponent implements OnInit {

  constructor(private tableService: TablesService,
              private snackBar: MatSnackBar,
              private dialog: MatDialog) { }

  columns=['ordinalNumber', 'nrOfSpots', 'actions'];
  dataSource : TableModel[] = [];
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
    this.tableService.get(this.page).toPromise().then(
      response => {
        this.dataSource = response!.data;
        this.page = response!.pagination!.currentPage;
        this.totalPages = response!.pagination!.total;
        this.next = response!.pagination!.links.next;
        this.previous = response!.pagination!.links.previous;
      }
    )
      .catch(_ => {
        this.snackBar.open('Tables not loaded!', 'OK', {duration: 3000});
      })
  }

  delete(id: number) {
    this.tableService.delete(id)
      .toPromise()
      .then(_ => {
        this.updatePage()
      })
      .catch(error => console.log(error))
  }

  edit(table: TableModel) {
    const dialogRef = this.dialog.open(
      TablesEditComponent, {
        data: {
          table: table
        }
      }
    );

    dialogRef.afterClosed().subscribe(response => {
      if (response != null) {
        this.tableService.update(table.id, response)
          .toPromise()
          .then(_ => this.updatePage())
          .catch(error => console.log(error));
      }
    })
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
      TablesAddComponent, {
        data: {
          table: new TableModel()
        }
      }
    );

    dialogRef.afterClosed().subscribe(response => {
      console.log(response)
      if (response != null) {
        this.tableService.add(response).toPromise()
          .then(_ => this.updatePage())
          .catch(error => console.log(error));
      }
    })
  }

}
