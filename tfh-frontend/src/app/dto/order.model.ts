import {MenuItemWithCountModel} from "./menu-item-with-count.model";


export class OrderModel {
  constructor(public bookingId: number = 0,
              public items: MenuItemWithCountModel[] = []) {
  }
}
