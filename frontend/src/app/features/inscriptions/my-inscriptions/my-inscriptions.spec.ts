import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyInscriptions } from './my-inscriptions';

describe('MyInscriptions', () => {
  let component: MyInscriptions;
  let fixture: ComponentFixture<MyInscriptions>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyInscriptions]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyInscriptions);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
