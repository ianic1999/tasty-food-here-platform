import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddOrderDialogComponent } from './add-order-dialog.component';

describe('AddOrderDialogComponent', () => {
  let component: AddOrderDialogComponent;
  let fixture: ComponentFixture<AddOrderDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddOrderDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddOrderDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
