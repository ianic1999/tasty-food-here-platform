import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MenuItemsAddComponent } from './menu-items-add.component';

describe('MenuItemsAddComponent', () => {
  let component: MenuItemsAddComponent;
  let fixture: ComponentFixture<MenuItemsAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MenuItemsAddComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MenuItemsAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
