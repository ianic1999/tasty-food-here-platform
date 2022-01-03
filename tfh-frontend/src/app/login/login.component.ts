import { Component, OnInit } from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {AuthenticationService} from "../service/authentication.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  phone: string = '';
  password: string = '';
  hide = true;

  constructor(private snackBar: MatSnackBar,
              private router: Router,
              private authenticationService: AuthenticationService) { }

  ngOnInit(): void {
  }

  login() {
    this.authenticationService.login(this.phone, this.password)
      .toPromise()
      .then(_ => {
        this.snackBar.open('Logged in', 'OK', {duration: 4000});
        this.router.navigate(['/dashboard/menu-items']);
      })
      .catch(error => {
        this.snackBar.open(error, 'OK', {duration: 4000})
      })

  }

}
