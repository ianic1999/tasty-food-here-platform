import { Component, OnInit } from '@angular/core';
import {MenuItemsService} from "../../service/menu-items.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialog} from "@angular/material/dialog";
import {MenuItemModel} from "../../model/menu-item-model";
import {MenuItemsAddComponent} from "../../menu-items/menu-items-add/menu-items-add.component";
import {FeedbackService} from "../../service/feedback.service";
import {FeedbackModel} from "../../dto/feedback.model";

@Component({
  selector: 'app-feedback-list',
  templateUrl: './feedback-list.component.html',
  styleUrls: ['./feedback-list.component.css']
})
export class FeedbackListComponent implements OnInit {

  constructor(private feedbackService: FeedbackService,
              private snackBar: MatSnackBar,
              private dialog: MatDialog) { }

  columns=['text', 'rating', 'client', 'actions'];
  dataSource : FeedbackModel[] = [];
  page = 1;
  totalPages = 5;
  previous = false;
  next = true;

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    this.updatePage();
  }

  updatePage() {
    this.feedbackService.get(this.page).toPromise().then(
      response => {
        this.dataSource = response!.data;
        this.page = response!.pagination!.currentPage;
        this.totalPages = response!.pagination!.total;
        this.next = response!.pagination!.links.next;
        this.previous = response!.pagination!.links.previous;
      }
    )
      .catch(_ => {
        this.snackBar.open('Feedback not loaded!', 'OK', {duration: 3000});
      })
  }

  delete(id: number) {
    this.feedbackService.delete(id)
      .toPromise()
      .then(_ => {
        this.updatePage()
      })
      .catch(error => console.log(error))
  }

  previousPage() {
    this.page -= 1;
    this.updatePage();
  }

  nextPage() {
    this.page += 1;
    this.updatePage();
  }

}
