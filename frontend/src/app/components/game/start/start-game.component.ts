import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {GameService} from "../game.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Categroy} from "../../categroy-editor/categroy";
import {ApiEndpoint} from "../../../config/apiEndpoint";
import {ApiHttpMethods} from "../../../config/apiHttpMethods";
import {WheelOfFortuneApiService} from "../../../api/wheel-of-fortune-api.service";

@Component({
  selector: "start-game",
  templateUrl: "start-game.component.html",
  styleUrls: ["start-game.component.scss"]
})
export class StartGameComponent implements OnInit {

  startForm: FormGroup;
  usernameExists: boolean = false;

  categories: Categroy[] = [];
  selectedCategoryId: number = -1;

  constructor(
    private gameService: GameService,
    private formBuilder: FormBuilder,
    private game: GameService,
    private api: WheelOfFortuneApiService,
  ) {
    this.startForm = formBuilder.group({
      username: ["", Validators.compose([
        Validators.minLength(2),
        Validators.maxLength(20),
        Validators.required,
      ])],
      category: ["", Validators.required],
    });
  }

  ngOnInit(): void {
    this.api.callHandled(ApiEndpoint.CATEGORY, {}, ApiHttpMethods.GET, false)
      .subscribe((receivedCategories: Categroy[]) => this.categories = receivedCategories);
  }

  play(){
    const username: string = this.startForm.value.username;
    const category: Categroy = this.findCategoryById(this.selectedCategoryId);
    this.game.startGame(username, category).then(() => this.usernameExists = false).catch(() => this.usernameExists = true);
  }

  private findCategoryById(id: number): Categroy{
    for (let c of this.categories) {
      if(c.id === id)
        return c;
    }
    return {id: -1, name: ""};
  }
}
