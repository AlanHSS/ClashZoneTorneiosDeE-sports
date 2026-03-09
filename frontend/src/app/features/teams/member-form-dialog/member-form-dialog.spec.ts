import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MemberFormDialog } from './member-form-dialog';

describe('MemberFormDialog', () => {
  let component: MemberFormDialog;
  let fixture: ComponentFixture<MemberFormDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MemberFormDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MemberFormDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
