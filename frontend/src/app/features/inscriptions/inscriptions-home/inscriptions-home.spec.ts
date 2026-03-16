import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscriptionsHome2 } from './inscriptions-home2';

describe('InscriptionsHome2', () => {
  let component: InscriptionsHome2;
  let fixture: ComponentFixture<InscriptionsHome2>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscriptionsHome2]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InscriptionsHome2);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
