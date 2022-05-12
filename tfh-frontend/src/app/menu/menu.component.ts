import { Component, OnInit } from '@angular/core';
import {MenuItemsService} from "../service/menu-items.service";
import {MenuItemPerCategoryDto} from "../dto/menu-item-per-category-dto";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  items: MenuItemPerCategoryDto[] = []

  constructor(private menuItemService: MenuItemsService,
              private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.loadMenu()
  }

  loadMenu() {
    this.menuItemService.getAllPerCategories().
    toPromise().then(
      response => {
        this.items = response?.data!
        console.log(response?.data)
      }
    )
      .catch(_ => {
        this.snackBar.open('Menu items not loaded!', 'OK', {duration: 3000});
      })
  }

  isNameTooLong(name: string) {
    return name.length >= 24;
  }

}
