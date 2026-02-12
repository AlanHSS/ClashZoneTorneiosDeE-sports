import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { LoadingService } from '@core/services/loading.service';

@Component({
  selector: 'app-spinner',
  standalone: true,
  imports: [CommonModule, MatProgressSpinnerModule],
  templateUrl: './spinner.html',
  styleUrl: './spinner.scss'
})
export class SpinnerComponent {
  loadingService = inject(LoadingService);
}
