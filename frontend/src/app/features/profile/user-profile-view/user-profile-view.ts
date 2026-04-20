import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { AuthService } from '@core/services/auth';
import { UserService } from '@core/services/user';
import { TournamentService } from '@core/services/tournament';
import { NotificationService } from '@core/services/notification';
import { TorneioDomain, UsuariosDomain } from '@core/models';
import { GameNamePipe } from '@shared/pipes/game-name-pipe';
import { StatusBadgePipe } from '@shared/pipes/status-badge-pipe';
import { StatusNamePipe } from '@shared/pipes/status-name-pipe';
import { PlatformNamePipe } from '@shared/pipes/platform-name-pipe';

@Component({
  selector: 'app-user-profile-view',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatProgressSpinnerModule,
    GameNamePipe,
    StatusBadgePipe,
    StatusNamePipe,
    PlatformNamePipe
  ],
  templateUrl: './user-profile-view.html',
  styleUrl: './user-profile-view.scss'
})
export class UserProfileViewComponent implements OnInit {
  private router = inject(Router);
  private authService = inject(AuthService);
  private userService = inject(UserService);
  private tournamentService = inject(TournamentService);
  private notificationService = inject(NotificationService);

  loading = true;
  user: UsuariosDomain | null = null;
  myTournaments: TorneioDomain[] = [];

  ngOnInit(): void {
    const current = this.authService.getCurrentUser();
    if (!current?.id) {
      this.router.navigate(['/auth/login']);
      return;
    }

    this.load(current.id);
  }

  onEdit(): void {
    this.router.navigate(['/profile/edit']);
  }

  private load(id: number): void {
    this.loading = true;

    this.userService.buscarPorId(id).subscribe({
      next: (data) => {
        this.user = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.notificationService.error('Erro ao carregar perfil');
        this.router.navigate(['/']);
      }
    });

    this.tournamentService.listarMeusTorneios().subscribe({
      next: (data) => {
        this.myTournaments = data || [];
      },
      error: () => {
        this.myTournaments = [];
      }
    });
  }
}

