import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {OrderModel} from "../dto/order.model";
import {MenuItemsService} from "../service/menu-items.service";
import {MenuItemPerCategoryDto} from "../dto/menu-item-per-category-dto";
import {MenuItemWithCountModel} from "../dto/menu-item-with-count.model";
import {OrderService} from "../service/order.service";
import {MatSnackBar} from "@angular/material/snack-bar";

export interface AddOrderData {
  bookingId: number
}

@Component({
  selector: 'app-add-order-dialog',
  templateUrl: './add-order-dialog.component.html',
  styleUrls: ['./add-order-dialog.component.css']
})
export class AddOrderDialogComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<AddOrderDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: AddOrderData,
              private menuService: MenuItemsService,
              private orderService: OrderService,
              private snackBar: MatSnackBar) { }

  request: OrderModel = new OrderModel()
  menu: MenuItemPerCategoryDto[] = []

  hoveredItem: number | null = null;
  price: number = 0;


  ngOnInit(): void {
    this.menuService.getAllPerCategories().toPromise()
      .then(response => this.menu = response?.data!)
      .catch(err => console.log(err))
  }

  close() {
    this.dialogRef.close();
  }

  submit() {
    this.request.bookingId = this.data.bookingId;
    this.dialogRef.close(this.request)
  }

  isNameTooLong(name: string): boolean {
    return name.length >= 24;
  }

  getCountForItem(itemId: number): number {
    let filtered = this.request.items.filter(i => i.menuItemId == itemId)
    if (filtered.length > 0) {
      return filtered[0].quantity
    }
    return 0
  }

  showAdd(itemId: number) {
    this.hoveredItem = itemId;
  }

  hideAdd() {
    this.hoveredItem = null;
  }

  add(itemId: number, itemPrice: number) {
    let filtered = this.request.items.filter(i => i.menuItemId == itemId)
    if (filtered.length > 0) {
      filtered[0].quantity += 1;
    } else {
      this.request.items.push(new MenuItemWithCountModel(itemId, 1))
    }
    this.price += itemPrice;
  }

  extract(itemId: number, itemPrice: number) {
    let filtered = this.request.items.filter(i => i.menuItemId == itemId)
    if (filtered.length > 0 && filtered[0].quantity > 1) {
      filtered[0].quantity -= 1;
      this.price -= itemPrice;
    } else if (filtered.length > 0 && filtered[0].quantity == 1){
      this.request.items = this.request.items.filter(i => i.menuItemId !== itemId)
      this.price -= itemPrice;
    }
  }

  confirm() {
    this.request.bookingId = this.data.bookingId;
    this.dialogRef.close(this.request)
  }

}
