import {PaginationLinksModel} from "./pagination-links.model";

export class PaginationModel {
  constructor(public count: number = 0,
              public total: number = 0,
              public perPage: number = 0,
              public currentPage: number = 0,
              public links: PaginationLinksModel = {
                previous: false,
                next: false
              }
              ) {
  }
}
