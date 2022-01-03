import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FoodCategoryModel} from "../../dto/food-category.model";
import {TableModel} from "../../dto/table.model";

export interface AddTablesData {
  table: TableModel
}

@Component({
  selector: 'app-tables-add',
  templateUrl: './tables-add.component.html',
  styleUrls: ['./tables-add.component.css']
})
export class TablesAddComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<TablesAddComponent>,
              @Inject(MAT_DIALOG_DATA) public data: AddTablesData) {
  }

  ngOnInit(): void {
  }

  submit() {
    this.dialogRef.close(this.data.table)
  }

  close() {
    this.dialogRef.close();
  }

}
