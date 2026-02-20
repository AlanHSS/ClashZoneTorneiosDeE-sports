import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { TournamentService } from '@core/services/tournament';
import { NotificationService } from '@core/services/notification';
import { AuthService } from '@core/services/auth';
import { TorneioDomain, Games, StatusDoTorneio, Plataforma } from '@core/models';
import { TournamentCardComponent } from '@shared/components/tournament-card/tournament-card';

@Component({
  selector: 'app-tournament-list',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    TournamentCardComponent
  ],
  templateUrl: './tournament-list.html',
  styleUrl: './tournament-list.scss'
})
export class TournamentListComponent implements OnInit {
  private tournamentService = inject(TournamentService);
  private notificationService = inject(NotificationService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  tournaments: TorneioDomain[] = [];
  filteredTournaments: TorneioDomain[] = [];
  filterForm: FormGroup;

  games = Object.values(Games);
  status = Object.values(StatusDoTorneio);
  plataformas = Object.values(Plataforma);

  showFilters = false;

  constructor() {
    this.filterForm = this.fb.group({
      nomeDoTorneio: [''],
      jogoDoTorneio: [''],
      statusDoTorneio: [''],
      plataforma: ['']
    });
  }

  ngOnInit(): void {
    this.loadTournaments();

    this.filterForm.valueChanges.subscribe(() => {
      this.applyFilters();
    });
  }

  loadTournaments(): void {
    this.tournamentService.listarTorneios().subscribe({
      next: (data) => {
        this.tournaments = data;
        this.filteredTournaments = data;
      },
      error: () => {
        this.notificationService.error('Erro ao carregar torneios');
      }
    });
  }

  applyFilters(): void {
    const filters = this.filterForm.value;

    this.filteredTournaments = this.tournaments.filter(tournament => {
      const matchName = !filters.nomeDoTorneio ||
        tournament.nomeDoTorneio.toLowerCase().includes(filters.nomeDoTorneio.toLowerCase());

      const matchGame = !filters.jogoDoTorneio ||
        tournament.jogoDoTorneio === filters.jogoDoTorneio;

      const matchStatus = !filters.statusDoTorneio ||
        tournament.statusDoTorneio === filters.statusDoTorneio;

      const matchPlatform = !filters.plataforma ||
        tournament.plataforma === filters.plataforma;

      return matchName && matchGame && matchStatus && matchPlatform;
    });
  }

  clearFilters(): void {
    this.filterForm.reset();
    this.filteredTournaments = this.tournaments;
  }

  toggleFilters(): void {
    this.showFilters = !this.showFilters;
  }

  onViewDetails(id: number): void {
    this.router.navigate(['/tournaments', id]);
  }

  onRegister(id: number): void {
    this.router.navigate(['/inscriptions/create'], {
      queryParams: { tournamentId: id }
    });
  }

  onCreateTournament(): void {
    this.router.navigate(['/tournaments/create']);
  }

  canCreateTournament(): boolean {
    return this.authService.isAdmin();
  }
}
