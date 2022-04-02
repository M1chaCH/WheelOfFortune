import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {MaterialModule} from "./material.module";
import {AdminComponent} from "./pages/admin/admin.component";
import {LoginComponent} from "./pages/login/login.component";
import {HomeComponent} from "./pages/home/home.component";
import {AuthInterceptor} from "./api/auth-interceptor";
import {ErrorComponent} from "./pages/error/error.component";
import {ErrorSnackComponent} from "./components/error-snack.component";
import {HighScoreListComponent} from "./components/high-score/high-score-list.component";
import {DeletedSnackComponent} from "./components/deleted-snack.component";
import {CategoryEditorComponent} from "./components/categroy-editor/category-editor.component";
import {SentenceEditor} from "./components/sentence-editor/sentence-editor";
import {
  SentenceEditorBottomSheetComponent
} from "./components/sentence-editor/bottom-sheet/sentence-editor-bottom-sheet.component";
import {CategoryAdderComponent} from "./components/categroy-editor/adder/category-adder.component";
import {CategoryAdderDialogComponent} from "./components/categroy-editor/adder/category-adder-dialog.component";
import {QuestionEditorComponent} from "./components/quesiton-editor/question-editor.component";
import {
  QuestionEditorBottomSheetComponent
} from "./components/quesiton-editor/bottom-sheet/question-editor-bottom-sheet.component";
import {StartGameComponent} from "./components/game/start/start-game.component";
import {PlayGameComponent} from "./components/game/play/play-game.component";
import {GameEndComponent} from "./components/game/end/game-end.component";
import {RiskComponent} from "./components/game/play/risk/risk.component";
import {CharSelectorComponent} from "./components/game/play/char-selector/char-selector.component";
import {BankruptDialogComponent} from "./components/game/dialogs/bankrupt-dialog.component";
import {HpDeathDialogComponent} from "./components/game/dialogs/hp-death-dialog.component";

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    AdminComponent,
    LoginComponent,
    ErrorComponent,
    ErrorSnackComponent,
    DeletedSnackComponent,
    HighScoreListComponent,
    CategoryEditorComponent,
    SentenceEditor,
    SentenceEditorBottomSheetComponent,
    CategoryAdderComponent,
    CategoryAdderDialogComponent,
    QuestionEditorComponent,
    QuestionEditorBottomSheetComponent,
    StartGameComponent,
    PlayGameComponent,
    GameEndComponent,
    RiskComponent,
    CharSelectorComponent,
    BankruptDialogComponent,
    HpDeathDialogComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
