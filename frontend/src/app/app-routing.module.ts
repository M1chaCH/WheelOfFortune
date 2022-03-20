import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AppRout} from "./config/appRout";
import {HomeComponent} from "./pages.home/home.component";

const routes: Routes = [
  { path: AppRout.EMPTY, redirectTo: AppRout.HOME, pathMatch: "full" },
  { path: AppRout.HOME, component: HomeComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
