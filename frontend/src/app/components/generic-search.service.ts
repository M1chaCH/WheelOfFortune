import {Injectable} from "@angular/core";

@Injectable({
  providedIn: "root"
})
export class GenericSearchService {

  search<Type extends any>(query: string, data: Type[]): Type[]{
    if(query === "")
      return data;

    let found: Type[] = [];
    for (let entry of data) {
      let fields = Object.keys(entry as any);
      for (let field of fields) {
        let value: string = (entry as any)[field] + "";
        if(value.toLowerCase().includes(query.toLowerCase())) {
          found.push(entry);
          break;
        }
      }
    }
    return found;
  }
}
