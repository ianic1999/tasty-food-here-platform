

export class UserModel {
  constructor(public id: number = 0,
              public firstName: string = '',
              public lastName: string = '',
              public phone: string = '',
              public email: string = '',
              public role: string = '',
              public confirmed: boolean = false) {
  }
}
