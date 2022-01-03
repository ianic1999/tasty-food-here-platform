import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {TableModel} from "../../dto/table.model";

export interface EditTablesData {
  table: TableModel
}

@Component({
  selector: 'app-tables-edit',
  templateUrl: './tables-edit.component.html',
  styleUrls: ['./tables-edit.component.css']
})
export class TablesEditComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<TablesEditComponent>,
              @Inject(MAT_DIALOG_DATA) public data: EditTablesData) {
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
