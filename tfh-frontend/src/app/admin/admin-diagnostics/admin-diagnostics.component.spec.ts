import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminDiagnosticsComponent } from './admin-diagnostics.component';

describe('AdminDiagnosticsComponent', () => {
  let component: AdminDiagnosticsComponent;
  let fixture: ComponentFixture<AdminDiagnosticsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminDiagnosticsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminDiagnosticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
