
export class FeedbackModel {
  constructor(public id: number = 0,
              public text: string = '',
              public rating: number = 0,
              public fullName: string = '') {
  }
}
