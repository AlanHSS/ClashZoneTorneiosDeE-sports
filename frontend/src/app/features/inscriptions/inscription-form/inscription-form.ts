import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { InscriptionService } from '@core/services/inscription';
import { TournamentService } from '@core/services/tournament';
import { TeamService } from '@core/services/team';
import { NotificationService } from '@core/services/notification';
import { TorneioDomain, EquipeDomain } from '@core/models';
import { GameNamePipe } from '@shared/pipes/game-name-pipe';

@Component({
  selector: 'app-inscription-form',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    GameNamePipe
  ],
  templateUrl: './inscription-form.html',
  styleUrl: './inscription-form.scss'
})
export class InscriptionFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private inscriptionService = inject(InscriptionService);
  private tournamentService = inject(TournamentService);
  private teamService = inject(TeamService);
  private notificationService = inject(NotificationService);

  inscriptionForm: FormGroup;
  tournament: TorneioDomain | null = null;
  allTeams: EquipeDomain[] = [];
  eligibleTeams: EquipeDomain[] = [];
  loadingTeams = false;

  constructor() {
    this.inscriptionForm = this.fb.group({
      equipeId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const tournamentId = this.route.snapshot.queryParamMap.get('tournamentId');

    if (!tournamentId) {
      this.notificationService.error('ID do torneio não fornecido');
      this.router.navigate(['/tournaments']);
      return;
    }

    this.loadTournament(Number(tournamentId));
    this.loadMyTeams();
  }

  loadTournament(id: number): void {
    this.tournamentService.buscarPorId(id).subscribe({
      next: (data) => {
        this.tournament = data;
        this.filterEligibleTeams();
      },
      error: () => {
        this.notificationService.error('Erro ao carregar torneio');
        this.router.navigate(['/tournaments']);
      }
    });
  }

  loadMyTeams(): void {
    this.loadingTeams = true;

    this.teamService.listarMinhasEquipes().subscribe({
      next: (teams) => {
        this.allTeams = teams;
        this.filterEligibleTeams();
        this.loadingTeams = false;
      },
      error: () => {
        this.loadingTeams = false;
      }
    });
  }

  filterEligibleTeams(): void {
    if (!this.tournament || this.allTeams.length === 0) return;

    // Filtrar apenas equipes do mesmo jogo do torneio
    this.eligibleTeams = this.allTeams.filter(
      team => team.jogo === this.tournament!.jogoDoTorneio
    );
  }

  getAvailableSlots(): number {
    if (!this.tournament) return 0;
    // Aqui você pode buscar as inscrições aprovadas e calcular
    // Por enquanto, retorna a quantidade total
    return this.tournament.quantidadeDeEquipes;
  }

  onSubmit(): void {
    if (this.inscriptionForm.valid && this.tournament) {
      const inscriptionData = {
        torneioId: this.tournament.id,
        equipeId: this.inscriptionForm.value.equipeId
      };

      this.inscriptionService.criar(inscriptionData).subscribe({
        next: () => {
          this.notificationService.success('Inscrição realizada com sucesso! Aguarde aprovação.');
          this.router.navigate(['/inscriptions/my']);
        }
      });
    } else {
      this.inscriptionForm.markAllAsTouched();
    }
  }

  onCancel(): void {
    if (this.tournament) {
      this.router.navigate(['/tournaments', this.tournament.id]);
    } else {
      this.router.navigate(['/tournaments']);
    }
  }

  getErrorMessage(field: string): string {
    const control = this.inscriptionForm.get(field);

    if (control?.hasError('required')) {
      return 'Este campo é obrigatório';
    }

    return '';
  }
}
