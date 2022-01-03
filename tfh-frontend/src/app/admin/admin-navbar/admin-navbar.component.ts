import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-navbar',
  templateUrl: './admin-navbar.component.html',
  styleUrls: ['./admin-navbar.component.css']
})
export class AdminNavbarComponent implements OnInit {

  selectedItem: number = 1;

  constructor(private router: Router) { }

  ngOnInit(): void {

  }

  navigate(path: string, itemOrder: number) {
    this.router.navigate(['/dashboard', path]);
    this.selectedItem = itemOrder;
  }

}
