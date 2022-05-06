import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookingConfirmComponent } from './booking-confirm.component';

describe('BookingConfirmComponent', () => {
  let component: BookingConfirmComponent;
  let fixture: ComponentFixture<BookingConfirmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BookingConfirmComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BookingConfirmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
