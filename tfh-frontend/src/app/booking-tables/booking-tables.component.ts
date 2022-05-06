import {Component, ComponentFactoryResolver, OnInit, ViewChild, ViewContainerRef} from '@angular/core';
import {TableModel} from "../dto/table.model";
import {MatSnackBar} from "@angular/material/snack-bar";
import {BookingService} from "../service/booking.service";
import {BookingDtoModel} from "../dto/booking-dto.model";
import {BookingConfirmComponent} from "../booking-confirm/booking-confirm.component";

@Component({
  selector: 'app-booking-tables',
  templateUrl: './booking-tables.component.html',
  styleUrls: ['./booking-tables.component.css']
})
export class BookingTablesComponent implements OnInit {

  @ViewChild('confirm', {read: ViewContainerRef})
  container!: ViewContainerRef

  constructor(private componentFactoryResolver: ComponentFactoryResolver,
              private bookingService: BookingService,
              private snackBar: MatSnackBar) { }
  tables: TableModel[] = [];
  selectedTable: number | null = null;
  date: string = '';
  time: string = '';
  duration: number = 0;
  phone: string = '';
  fullName: string = '';

  ngOnInit(): void {
    if (this.tables.length == 0) {
      this.snackBar.open('There are no free tables for this time', 'OK', {duration: 4000})
    }
  }

  select(tableId: number) {
    this.selectedTable = tableId;
  }

  book() {
    let booking = new BookingDtoModel(
    null,
      this.phone,
      this.fullName,
      this.date,
      this.time,
      this.duration,
      this.selectedTable!
    )
    this.bookingService.book(booking).toPromise()
      .then(response => this.showConfirmation(response?.data!))
  }

  showConfirmation(booking: any) {
    const factory = this.componentFactoryResolver.resolveComponentFactory(BookingConfirmComponent);
    this.container.clear()
    let componentRef = this.container.createComponent(factory)
    componentRef.instance.phone = this.phone
    componentRef.instance.bookingId = booking.id;
  }

  canBook(): boolean {
    return this.phone !== '' && this.fullName !== '' && this.selectedTable != null;
  }

}
