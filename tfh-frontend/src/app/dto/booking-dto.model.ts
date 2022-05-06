
export class BookingDtoModel {
  constructor(public id: number|null = null,
  public phone: string = '',
  public fullName: string = '',
  public date: string = '',
  public time: string = '',
  public duration: number = 0,
  public tableId: number = 0) {
  }
}
