import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { TournamentService } from '@core/services/tournament';
import { NotificationService } from '@core/services/notification';
import { Games, StatusDoTorneio, Plataforma } from '@core/models';

@Component({
  selector: 'app-tournament-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './tournament-form.html',
  styleUrl: './tournament-form.scss'
})
export class TournamentFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private tournamentService = inject(TournamentService);
  private notificationService = inject(NotificationService);

  tournamentForm: FormGroup;
  isEditMode = false;
  tournamentId: number | null = null;

  games = Object.values(Games);
  status = Object.values(StatusDoTorneio);
  plataformas = Object.values(Plataforma);

  minDate = new Date();

  constructor() {
    this.tournamentForm = this.fb.group({
      nomeDoTorneio: ['', [Validators.required, Validators.minLength(3)]],
      descricaoDoTorneio: [''],
      inicioDoTorneio: ['', Validators.required],
      jogoDoTorneio: ['', Validators.required],
      quantidadeDeEquipes: ['', [Validators.required, Validators.min(2), Validators.max(128)]],
      statusDoTorneio: ['AGENDADO', Validators.required],
      plataforma: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.tournamentId = Number(id);
      this.loadTournament(this.tournamentId);
    }
  }

  loadTournament(id: number): void {
    this.tournamentService.buscarPorId(id).subscribe({
      next: (data) => {
        this.tournamentForm.patchValue({
          nomeDoTorneio: data.nomeDoTorneio,
          descricaoDoTorneio: data.descricaoDoTorneio,
          inicioDoTorneio: new Date(data.inicioDoTorneio),
          jogoDoTorneio: data.jogoDoTorneio,
          quantidadeDeEquipes: data.quantidadeDeEquipes,
          statusDoTorneio: data.statusDoTorneio,
          plataforma: data.plataforma
        });
      },
      error: () => {
        this.notificationService.error('Erro ao carregar torneio');
        this.router.navigate(['/tournaments']);
      }
    });
  }

  onSubmit(): void {
    if (this.tournamentForm.valid) {
      const formData = {
        ...this.tournamentForm.value,
        inicioDoTorneio: new Date(this.tournamentForm.value.inicioDoTorneio).toISOString()
      };

      if (this.isEditMode && this.tournamentId) {
        this.updateTournament(this.tournamentId, formData);
      } else {
        this.createTournament(formData);
      }
    } else {
      this.tournamentForm.markAllAsTouched();
    }
  }

  createTournament(data: any): void {
    this.tournamentService.criar(data).subscribe({
      next: () => {
        this.notificationService.success('Torneio criado com sucesso!');
        this.router.navigate(['/tournaments']);
      },
      error: () => {
        // Erro já tratado pelo error interceptor
      }
    });
  }

  updateTournament(id: number, data: any): void {
    this.tournamentService.atualizar(id, data).subscribe({
      next: () => {
        this.notificationService.success('Torneio atualizado com sucesso!');
        this.router.navigate(['/tournaments', id]);
      },
      error: () => {
        // Erro já tratado pelo error interceptor
      }
    });
  }

  onCancel(): void {
    if (this.isEditMode && this.tournamentId) {
      this.router.navigate(['/tournaments', this.tournamentId]);
    } else {
      this.router.navigate(['/tournaments']);
    }
  }

  getErrorMessage(field: string): string {
    const control = this.tournamentForm.get(field);

    if (control?.hasError('required')) {
      return 'Este campo é obrigatório';
    }

    if (control?.hasError('minlength')) {
      const minLength = control.errors?.['minlength'].requiredLength;
      return `Mínimo de ${minLength} caracteres`;
    }

    if (control?.hasError('min')) {
      const min = control.errors?.['min'].min;
      return `Valor mínimo: ${min}`;
    }

    if (control?.hasError('max')) {
      const max = control.errors?.['max'].max;
      return `Valor máximo: ${max}`;
    }

    return '';
  }
}
