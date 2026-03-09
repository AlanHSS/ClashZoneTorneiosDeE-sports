import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TeamService } from '@core/services/team';
import { NotificationService } from '@core/services/notification';
import { AuthService } from '@core/services/auth';
import { EquipeDomain, Games } from '@core/models';
import { GameNamePipe } from '@shared/pipes/game-name-pipe';

@Component({
  selector: 'app-team-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    GameNamePipe
  ],
  templateUrl: './team-list.html',
  styleUrl: './team-list.scss'
})
export class TeamListComponent implements OnInit {
  private teamService = inject(TeamService);
  private notificationService = inject(NotificationService);
  private authService = inject(AuthService);
  private router = inject(Router);

  teams: EquipeDomain[] = [];
  filteredTeams: EquipeDomain[] = [];
  loading = true;

  games = Object.values(Games);

  filters = {
    search: '',
    game: null as Games | null
  };

  ngOnInit(): void {
    this.loadTeams();
  }

  loadTeams(): void {
    this.loading = true;

    const loadMethod = this.isAdmin()
      ? this.teamService.listarEquipes()
      : this.teamService.listarMinhasEquipes();

    loadMethod.subscribe({
      next: (data) => {
        this.teams = data;
        this.filteredTeams = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  applyFilters(): void {
    let filtered = [...this.teams];

    // Filtro de busca por nome
    if (this.filters.search) {
      const search = this.filters.search.toLowerCase();
      filtered = filtered.filter(team =>
        team.nomeDaEquipe.toLowerCase().includes(search)
      );
    }

    // Filtro por jogo
    if (this.filters.game) {
      filtered = filtered.filter(team => team.jogo === this.filters.game);
    }

    this.filteredTeams = filtered;
  }

  clearFilters(): void {
    this.filters = {
      search: '',
      game: null
    };
    this.applyFilters();
  }

  hasActiveFilters(): boolean {
    return this.filters.search !== '' || this.filters.game !== null;
  }

  onEdit(event: Event, teamId: number): void {
    event.stopPropagation();
    this.router.navigate(['/teams/edit', teamId]);
  }

  onDelete(event: Event, team: EquipeDomain): void {
    event.stopPropagation();

    if (confirm(`Tem certeza que deseja excluir a equipe "${team.nomeDaEquipe}"?`)) {
      this.teamService.deletar(team.id).subscribe({
        next: () => {
          this.notificationService.success('Equipe excluída com sucesso!');
          this.loadTeams();
        }
      });
    }
  }

  canCreateTeam(): boolean {
    return this.authService.isAuthenticated();
  }

  canManageTeam(team: EquipeDomain): boolean {
    const currentUser = this.authService.getCurrentUser();
    return currentUser?.id === team.liderId || this.authService.isAdmin();
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }
}
