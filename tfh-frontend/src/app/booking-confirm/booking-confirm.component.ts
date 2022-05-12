import {Component, ComponentFactoryResolver, OnInit} from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import {BookingConfirmationModel} from "../dto/booking-confirmation.model";
import {BookingService} from "../service/booking.service";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {AddOrderDialogComponent} from "../add-order-dialog/add-order-dialog.component";
import {OrderService} from "../service/order.service";

@Component({
  selector: 'app-booking-confirm',
  templateUrl: './booking-confirm.component.html',
  styleUrls: ['./booking-confirm.component.css']
})
export class BookingConfirmComponent implements OnInit {

  constructor(private bookingService: BookingService,
              private snackBar: MatSnackBar,
              private router: Router,
              private dialog: MatDialog,
              private orderService: OrderService) { }

  bookingId: number|null = null;
  phone: string = '';
  code: string = '';

  ngOnInit(): void {
  }

  confirm() {
    let request = new BookingConfirmationModel(this.bookingId!, this.code);
    this.bookingService.confirm(this.bookingId!, request).toPromise()
      .then(_ => this.handleSuccessConfirmation())
      .catch(err => this.snackBar.open(err, "OK", {duration: 4000}))
  }

  handleSuccessConfirmation() {
    this.snackBar.open('Booking successfully confirmed!', 'OK', {duration: 4000})
    this.openOrderDialog()
  }

  openOrderDialog() {
    const dialogRef = this.dialog.open(AddOrderDialogComponent, {
      data: {bookingId: this.bookingId}
    })
    dialogRef.afterClosed().subscribe(order => {
      if (order != null) {
        if (order.items.length > 0) {
          this.orderService.add(order).toPromise()
            .then(_ => {
              this.snackBar.open('Order added!', 'OK', {duration: 4000})
              this.router.navigate(['/home/menu']);
            })
            .catch(err => {
              console.log(err)
              this.snackBar.open("Connection error.", "OK", {duration: 4000})
              this.router.navigate(['/home/menu']);
            })
        }
      }
    })
  }
}
