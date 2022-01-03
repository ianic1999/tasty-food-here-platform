import {Component, ElementRef, Inject, OnInit, ViewChild} from '@angular/core';
import {MenuItemModel} from "../../model/menu-item-model";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FoodCategoryModel} from "../../dto/food-category.model";
import {MenuItemsService} from "../../service/menu-items.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-menu-items-add',
  templateUrl: './menu-items-add.component.html',
  styleUrls: ['./menu-items-add.component.css']
})
export class MenuItemsAddComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<MenuItemsAddComponent>,
              @Inject(MAT_DIALOG_DATA) public data: FormData,
              private menuItemsService: MenuItemsService,
              private snackBar: MatSnackBar) {
  }

  categories: FoodCategoryModel[] = [];
  name: string = '';
  price: number = 0.0;
  category: string = '';
  fileAttr = 'Choose file';

  ngOnInit(): void {
    this.menuItemsService.getFoodCategories()
      .toPromise()
      .then(result => this.categories = result!.data)
      .catch(_ => this.snackBar.open('Categories not loaded.', 'OK', {duration: 1000}))
  }

  uploadFile(event: any) {
    console.log(event)
    const file = event.target.files[0];
    console.log(file)
    this.data.append('image', file);
  }

  submit() {
    this.data.append('name', this.name);
    this.data.append('price', this.price.toString());
    this.data.append('category', this.category);
    console.log(this.data)
    this.dialogRef.close(this.data)
  }

  close() {
    this.dialogRef.close();
  }

}
