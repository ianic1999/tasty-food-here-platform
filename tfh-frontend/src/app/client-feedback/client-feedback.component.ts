import { Component, OnInit } from '@angular/core';
import {FeedbackService} from "../service/feedback.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {FeedbackModel} from "../dto/feedback.model";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-client-feedback',
  templateUrl: './client-feedback.component.html',
  styleUrls: ['./client-feedback.component.css']
})
export class ClientFeedbackComponent implements OnInit {

  constructor(private feedbackService: FeedbackService,
              private snackBar: MatSnackBar,
              private router: Router) { }

  bookingId: string = '';
  comments: string = '';
  rating: number = 0;

  ngOnInit(): void {
  }

  add() {
    let feedback: FeedbackModel = new FeedbackModel(this.comments, 5, this.bookingId);
    this.feedbackService.add(feedback)
      .toPromise()
      .then(_ => {
        this.snackBar.open('Feedback submitted. Thank you!', 'OK', {duration: 4000})
        this.router.navigate(['/home/menu']);
      })
      .catch(error => {
        console.log(error)
      })
  }

}
