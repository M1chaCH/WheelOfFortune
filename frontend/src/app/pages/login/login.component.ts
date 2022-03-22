import {Component, OnInit} from "@angular/core";
import {FormBuilder, FormGroup} from "@angular/forms";
import {AppRout} from "../../config/appRout";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../auth/auth.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ErrorSnackComponent} from "../../components/error-snack.component";

@Component({
  selector: "admin",
  templateUrl: "login.component.html",
  styleUrls: ["login.component.scss"]
})
export class LoginComponent implements OnInit{

  loginForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private auth: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private errorSnackBar: MatSnackBar,
  ) {
    this.loginForm = formBuilder.group({
      username: "",
      password: "",
    });
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      let errorMessage = params["message"];
      if(!!errorMessage) {
        this.errorSnackBar.openFromComponent(ErrorSnackComponent, {duration: 5 * 1000, data: errorMessage});
        this.router.navigate([AppRout.LOGIN]); //to reset the url params
      }
    })
  }

  login(){
    const username = this.loginForm.value.username;
    const password = this.loginForm.value.password;

    this.auth.login(username, password);
  }

  redirectHome(){
    this.router.navigate([AppRout.HOME]);
  }
}
