import {Component, ComponentFactoryResolver, OnInit} from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import {BookingConfirmationModel} from "../dto/booking-confirmation.model";
import {BookingService} from "../service/booking.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-booking-confirm',
  templateUrl: './booking-confirm.component.html',
  styleUrls: ['./booking-confirm.component.css']
})
export class BookingConfirmComponent implements OnInit {

  constructor(private bookingService: BookingService,
              private snackBar: MatSnackBar,
              private router: Router) { }

  bookingId: number|null = null;
  phone: string = '';
  code: string = '';

  ngOnInit(): void {
  }

  confirm() {
    let request = new BookingConfirmationModel(this.bookingId!, this.code);
    this.bookingService.confirm(this.bookingId!, request).toPromise()
      .then(_ => this.handleSuccessConfirmation())
  }

  handleSuccessConfirmation() {
    this.snackBar.open('Booking successfully confirmed!', 'OK', {duration: 4000})
    this.router.navigate(['/home/menu']);
  }
}
