import { TestBed } from '@angular/core/testing';
import { MatSnackBar } from '@angular/material/snack-bar';

import { ToastService } from './toast';

describe('ToastService', () => {
  let service: ToastService;
  const snackBarMock = {
    open: jasmine.createSpy('open')
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        { provide: MatSnackBar, useValue: snackBarMock }
      ]
    });

    snackBarMock.open.calls.reset();
    service = TestBed.inject(ToastService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
