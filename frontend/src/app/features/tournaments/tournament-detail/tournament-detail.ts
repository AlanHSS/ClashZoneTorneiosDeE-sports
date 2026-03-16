import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { TournamentService } from '@core/services/tournament';
import { InscriptionService } from '@core/services/inscription';
import { UserService } from '@core/services/user';
import { NotificationService } from '@core/services/notification';
import { AuthService } from '@core/services/auth';
import { TorneioDomain, InscricaoDetalhadaDto } from '@core/models';
import { GameNamePipe } from '@shared/pipes/game-name-pipe';
import { StatusBadgePipe } from '@shared/pipes/status-badge-pipe';
import { StatusNamePipe } from '@shared/pipes/status-name-pipe';
import { PlatformNamePipe } from '@shared/pipes/platform-name-pipe';

@Component({
  selector: 'app-tournament-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatDividerModule,
    GameNamePipe,
    StatusBadgePipe,
    StatusNamePipe,
    PlatformNamePipe
  ],
  templateUrl: './tournament-detail.html',
  styleUrl: './tournament-detail.scss'
})
export class TournamentDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private tournamentService = inject(TournamentService);
  private inscriptionService = inject(InscriptionService);
  private userService = inject(UserService);
  private notificationService = inject(NotificationService);
  private authService = inject(AuthService);

  tournament: TorneioDomain | null = null;
  inscriptions: InscricaoDetalhadaDto[] = [];
  criadorNome: string = '';
  loading = true;
  leaderNameById: Record<number, string> = {};

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadTournament(id);
    }
  }

  loadTournament(id: number): void {
    this.tournamentService.buscarPorId(id).subscribe({
      next: (data) => {
        this.tournament = data;
        this.loading = false;

        // Buscar nome do criador
        if (data.criadorId) {
          const currentUser = this.authService.getCurrentUser();
          const canLoadProfile = this.authService.isAdmin() || currentUser?.id === data.criadorId;

          if (canLoadProfile) {
            this.loadCriador(data.criadorId);
          } else {
            // Usa endpoint publico (nao-sensivel) para exibir nickname do organizador sem exigir permissao de perfil.
            this.loadCriadorPublico(data.criadorId);
          }
        }

        // Inscricoes do torneio sao restritas (ADMIN ou criador).
        // Evita chamar o endpoint quando o usuario nao tem permissao, para nao quebrar a pagina.
        if (this.canManageInscriptionsFor(data)) {
          this.loadInscriptions(data.id);
        } else {
          this.inscriptions = [];
        }
      },
      error: () => {
        this.notificationService.error('Erro ao carregar torneio');
        this.router.navigate(['/tournaments']);
      }
    });
  }

  loadCriador(criadorId: number): void {
    this.userService.buscarPorId(criadorId).subscribe({
      next: (usuario) => {
        this.criadorNome = usuario.nickname || usuario.nomeDoUsuario;
      },
      error: () => {
        this.criadorNome = 'Desconhecido';
      }
    });
  }

  loadCriadorPublico(criadorId: number): void {
    this.userService.buscarPublicoPorId(criadorId).subscribe({
      next: (usuario) => {
        this.criadorNome = usuario.nickname || usuario.nomeDoUsuario || 'Desconhecido';
      },
      error: () => {
        this.criadorNome = 'Desconhecido';
      }
    });
  }

  loadInscriptions(id: number): void {
    this.inscriptionService.listarPorTorneio(id).subscribe({
      next: (response) => {
        this.inscriptions = this.extractInscriptions(response);
        this.loadLeadersPublic(this.inscriptions);
      },
      error: () => {
        // Não exibe erro se não conseguir carregar inscrições
        this.inscriptions = [];
      }
    });
  }

  private extractInscriptions(response: any): InscricaoDetalhadaDto[] {
    if (!response) return [];
    if (Array.isArray(response)) return response as InscricaoDetalhadaDto[];

    const possibleKeys = ['Inscrições', 'Inscricoes', 'inscricoes', 'inscrições'];
    for (const key of possibleKeys) {
      const value = response?.[key];
      if (Array.isArray(value)) return value as InscricaoDetalhadaDto[];
    }

    for (const value of Object.values(response)) {
      if (Array.isArray(value)) return value as InscricaoDetalhadaDto[];
    }

    return [];
  }

  private loadLeadersPublic(inscriptions: InscricaoDetalhadaDto[]): void {
    const leaderIds = Array.from(new Set(inscriptions.map((i) => i.liderId).filter(Boolean)));

    for (const id of leaderIds) {
      if (this.leaderNameById[id]) continue;

      this.userService.buscarPublicoPorId(id).subscribe({
        next: (u) => {
          this.leaderNameById[id] = u.nickname || u.nomeDoUsuario || `Usuário #${id}`;
        },
        error: () => {
          this.leaderNameById[id] = `Usuário #${id}`;
        }
      });
    }
  }

  getLeaderName(liderId: number): string {
    return this.leaderNameById[liderId] || `Usuário #${liderId}`;
  }

  onEdit(): void {
    if (this.tournament) {
      this.router.navigate(['/tournaments/edit', this.tournament.id]);
    }
  }

  onDelete(): void {
    if (!this.tournament) return;

    if (confirm(`Tem certeza que deseja excluir o torneio "${this.tournament.nomeDoTorneio}"?`)) {
      this.tournamentService.deletar(this.tournament.id).subscribe({
        next: () => {
          this.notificationService.success('Torneio excluído com sucesso!');
          this.router.navigate(['/tournaments']);
        },
        error: () => {
          // Error interceptor já mostrou a mensagem
        }
      });
    }
  }

  onRegister(): void {
    if (this.tournament) {
      this.router.navigate(['/inscriptions/create'], {
        queryParams: { tournamentId: this.tournament.id }
      });
    }
  }

  onBack(): void {
    this.router.navigate(['/tournaments']);
  }

  onManageInscriptions(): void {
    if (!this.tournament) return;
    this.router.navigate(['/inscriptions/tournament', this.tournament.id]);
  }

  canEdit(): boolean {
    if (!this.tournament) return false;
    const currentUser = this.authService.getCurrentUser();
    return currentUser?.id === this.tournament.criadorId || this.authService.isAdmin();
  }

  canManageInscriptions(): boolean {
    if (!this.tournament) return false;
    return this.canManageInscriptionsFor(this.tournament);
  }

  private canManageInscriptionsFor(tournament: TorneioDomain): boolean {
    const currentUser = this.authService.getCurrentUser();
    return this.authService.isAdmin() || (!!currentUser && currentUser.id === tournament.criadorId);
  }

  canRegister(): boolean {
    return this.tournament?.statusDoTorneio === 'AGENDADO';
  }

  getInscriptionsCount(): number {
    if (!this.canManageInscriptions()) return 0;
    return this.inscriptions.filter(i => i.statusInscricao === 'APROVADA').length;
  }

  getAvailableSlots(): number {
    if (!this.tournament) return 0;
    if (!this.canManageInscriptions()) return this.tournament.quantidadeDeEquipes;
    return this.tournament.quantidadeDeEquipes - this.getInscriptionsCount();
  }
}
