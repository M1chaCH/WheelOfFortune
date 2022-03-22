import {Categroy} from "../categroy-editor/categroy";

export interface Question{
  id: number,
  question: string,
  answerOne: string,
  answerTwo: string,
  answerOneCorrect: boolean,
  categoryDTO: Categroy,
}
