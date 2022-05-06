
export class FeedbackModel {
  constructor(public text: string = '',
              public rating: number = 0,
              public bookingId: string = '') {
  }
}
