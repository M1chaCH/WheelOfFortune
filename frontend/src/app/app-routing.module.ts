import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AppRout} from "./config/appRout";
import {HomeComponent} from "./pages/home/home.component";
import {LoginComponent} from "./pages/login/login.component";
import {AdminComponent} from "./pages/admin/admin.component";
import {ErrorComponent} from "./pages/error/error.component";
import {AdminGuard} from "./auth/guards/admin.guard";

const routes: Routes = [
  { path: AppRout.EMPTY, redirectTo: AppRout.HOME, pathMatch: "full" },
  { path: AppRout.HOME, component: HomeComponent },
  { path: AppRout.LOGIN, component: LoginComponent },
  { path: AppRout.ADMIN, component: AdminComponent, canActivate: [AdminGuard] },
  { path: AppRout.ERROR, component: ErrorComponent},
  { path: '**', redirectTo: AppRout.HOME }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
