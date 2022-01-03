import {PaginationModel} from "./pagination.model";

export class PaginatedResponseModel<T> {
  constructor(public data: T[] = [],
              public pagination: PaginationModel) {
  }
}
