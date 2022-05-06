import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookingTablesComponent } from './booking-tables.component';

describe('BookingTablesComponent', () => {
  let component: BookingTablesComponent;
  let fixture: ComponentFixture<BookingTablesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BookingTablesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BookingTablesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
