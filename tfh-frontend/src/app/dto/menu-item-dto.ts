
export class MenuItemDto {
  constructor(public id: number = 0,
              public name: string = '',
              public price: number = 0,
              public image: string = '',
              public category: string = '') {
  }
}
