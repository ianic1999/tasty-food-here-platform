import {MenuItemDto} from "./menu-item-dto";

export class MenuItemPerCategoryDto {
  constructor(public category: string = '',
              public items: MenuItemDto[] = []) {
  }
}
