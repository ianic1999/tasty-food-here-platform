import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TablesAddComponent } from './tables-add.component';

describe('TablesAddComponent', () => {
  let component: TablesAddComponent;
  let fixture: ComponentFixture<TablesAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TablesAddComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TablesAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
