import {Categroy} from "../categroy-editor/categroy";

export interface Sentence{
  id: number,
  sentence: string,
  categoryDTO: Categroy,
}
