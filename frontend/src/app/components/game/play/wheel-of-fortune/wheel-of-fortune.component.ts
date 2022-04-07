import {Component, Input} from "@angular/core";
import {GameStateTask, WheelOfFortuneField} from "../../GameEntities";
import {Observable} from "rxjs";

export const WheelOfFortuneTimings = {
  GROW_DURATION: 500,
  SPIN_DURATION: 2000,
};

@Component({
  selector: "wheel-of-fortune",
  templateUrl: "wheel-of-fortune.component.html",
  styleUrls: ["wheel-of-fortune.component.scss"],
})
export class WheelOfFortuneComponent{
  wheelClass: string = "wheel-of-fortune-wheel";
  @Input()
  showWheel: boolean = false;

  private currentWheelOfFortuneField: WheelOfFortuneField | undefined;

  @Input("currentField") set currentField(field: WheelOfFortuneField | undefined){
    if(field === undefined)
      this.currentWheelOfFortuneField = undefined;
    else
      this.currentWheelOfFortuneField = field;

  }

  animate(): Observable<boolean>{
    return new Observable<boolean>((subscriber) => {
      this.showWheel = true;
      setTimeout(() => {
        this.wheelClass = "wheel-of-fortune-wheel-spin";
        setTimeout(() => {
          this.wheelClass = "wheel-of-fortune-leave";
          setTimeout(() => {
            this.showWheel = false;
            this.wheelClass = "wheel-of-fortune-wheel";
            subscriber.next(true);
          }, WheelOfFortuneTimings.GROW_DURATION);
        }, WheelOfFortuneTimings.SPIN_DURATION);
      }, WheelOfFortuneTimings.GROW_DURATION);
    })
  }

  wheelOfFortuneWheelClass(){
    if(this.currentWheelOfFortuneField === undefined)
      return "wheel-of-fortune-wheel";
    return "wheel-of-fortune-result";
  }

  wheelOfFortuneImageName(): string{
    if(this.currentWheelOfFortuneField?.task === GameStateTask.GUESS_CONSONANT) {
      return "wheeloffortune-" + this.currentWheelOfFortuneField?.reward;
    }
    return "wheeloffortune-" + this.currentWheelOfFortuneField?.task.toLocaleLowerCase();
  }

  showWheelOfFortuneField(): boolean{
    return this.currentWheelOfFortuneField?.task !== undefined && !this.showWheel;
  }
}
