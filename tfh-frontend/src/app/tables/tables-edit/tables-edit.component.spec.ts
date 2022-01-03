import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TablesEditComponent } from './tables-edit.component';

describe('TablesEditComponent', () => {
  let component: TablesEditComponent;
  let fixture: ComponentFixture<TablesEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TablesEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TablesEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
