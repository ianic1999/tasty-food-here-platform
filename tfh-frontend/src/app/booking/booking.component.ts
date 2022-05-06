import {Component, ComponentFactoryResolver, OnInit, ViewChild, ViewContainerRef} from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDatepickerInputEvent} from "@angular/material/datepicker";
import {TableBookingService} from "../service/table-booking.service";
import {TableBookingModel} from "../dto/table-booking.model";
import {TableModel} from "../dto/table.model";
import {DatePipe} from "@angular/common";
import {Router} from "@angular/router";
import {BookingTablesComponent} from "../booking-tables/booking-tables.component";

@Component({
  selector: 'app-booking',
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.css']
})
export class BookingComponent implements OnInit {

  @ViewChild('tables', {read: ViewContainerRef})
  container!: ViewContainerRef

  constructor(private bookingService: TableBookingService,
              private snackBar: MatSnackBar,
              private datePipe: DatePipe,
              private componentFactoryResolver: ComponentFactoryResolver) { }

  date = ''
  time = ''
  duration = 0

  ngOnInit(): void {
  }

  datePick(event: MatDatepickerInputEvent<Date>) {
    let date = event.value
    this.date = this.datePipe.transform(date,"yyyy/MM/dd")!;
    console.log(this.date)
  }

  timePick(event: any) {
    this.time = event.target.value;
  }

  durationPick(event: any) {
    this.duration = event.target.value;
  }

  get() {
    let request = new TableBookingModel(this.date, this.time, this.duration)
    this.bookingService.getFreeTables(request).toPromise()
      .then(response => {
        this.handleResponse(response!.data)
      })
      .catch(error => {
        console.log(error)
      })
  }

  handleResponse(tables: TableModel[]) {
    const factory = this.componentFactoryResolver.resolveComponentFactory(BookingTablesComponent);
    this.container.clear()
    let componentRef = this.container.createComponent(factory)
    componentRef.instance.tables = tables
    componentRef.instance.date = this.date
    componentRef.instance.time = this.time
    componentRef.instance.duration = this.duration
  }

  canSearch() {
    return this.date !== '' && this.time !== '' && this.duration > 0;
  }

}
