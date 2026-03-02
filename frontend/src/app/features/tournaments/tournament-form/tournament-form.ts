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
import { GameNamePipe } from '@shared/pipes/game-name-pipe';
import { StatusNamePipe } from '@shared/pipes/status-name-pipe';
import { PlatformNamePipe } from '@shared/pipes/platform-name-pipe';

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
    MatNativeDateModule,
    GameNamePipe,
    StatusNamePipe,
    PlatformNamePipe
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

  // ✅ QUANTIDADES PREDEFINIDAS
  quantidadesDisponiveis = [4, 8, 16, 32, 64, 128];

  minDate = new Date();

  constructor() {
    this.tournamentForm = this.fb.group({
      nomeDoTorneio: ['', [Validators.required, Validators.minLength(3)]],
      descricaoDoTorneio: [''],
      inicioDoTorneio: ['', Validators.required],
      jogoDoTorneio: ['', Validators.required],
      quantidadeDeEquipes: ['', Validators.required], // Sem min/max, apenas required
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
          inicioDoTorneio: new Date(data.inicioDoTorneio).toISOString().slice(0, 16),
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
    console.log('🔍 DEBUG - onSubmit chamado');
    console.log('📋 Form válido?', this.tournamentForm.valid);
    console.log('📋 Form values:', this.tournamentForm.value);

    if (this.tournamentForm.valid) {
      const rawData = this.tournamentForm.value;
      console.log('📤 Dados brutos do form:', rawData);

      const formData = {
        nomeDoTorneio: rawData.nomeDoTorneio,
        descricaoDoTorneio: rawData.descricaoDoTorneio || null,
        inicioDoTorneio: new Date(rawData.inicioDoTorneio).toISOString(),
        jogoDoTorneio: rawData.jogoDoTorneio,
        quantidadeDeEquipes: Number(rawData.quantidadeDeEquipes),
        statusDoTorneio: rawData.statusDoTorneio,
        plataforma: rawData.plataforma
      };

      console.log('📤 Dados formatados para envio:', formData);

      if (this.isEditMode && this.tournamentId) {
        this.updateTournament(this.tournamentId, formData);
      } else {
        this.createTournament(formData);
      }
    } else {
      console.log('❌ Formulário inválido');
      this.tournamentForm.markAllAsTouched();
    }
  }

  createTournament(data: any): void {
    console.log('🚀 Enviando requisição para criar torneio:', data);

    this.tournamentService.criar(data).subscribe({
      next: (response) => {
        console.log('✅ Torneio criado com sucesso:', response);
        this.notificationService.success('Torneio criado com sucesso!');
        this.router.navigate(['/tournaments']);
      }
      // Error interceptor cuida dos erros
    });
  }

  updateTournament(id: number, data: any): void {
    console.log('🔄 Atualizando torneio:', id, data);

    this.tournamentService.atualizar(id, data).subscribe({
      next: (response) => {
        console.log('✅ Torneio atualizado com sucesso:', response);
        this.notificationService.success('Torneio atualizado com sucesso!');
        this.router.navigate(['/tournaments', id]);
      }
      // Error interceptor cuida dos erros
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

    return '';
  }
}
