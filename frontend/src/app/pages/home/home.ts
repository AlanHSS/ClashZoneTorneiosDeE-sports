import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { AuthService } from '@core/services/auth';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule
  ],
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class HomeComponent {
  private router = inject(Router);
  authService = inject(AuthService);

  features = [
    {
      icon: 'emoji_events',
      title: 'Torneios Épicos',
      description: 'Participe de torneios organizados dos seus jogos favoritos'
    },
    {
      icon: 'groups',
      title: 'Monte sua Equipe',
      description: 'Crie e gerencie equipes competitivas com seus amigos'
    },
    {
      icon: 'leaderboard',
      title: 'Rankings',
      description: 'Acompanhe seu desempenho e suba no ranking'
    },
    {
      icon: 'workspace_premium',
      title: 'Prêmios',
      description: 'Conquiste títulos e reconhecimento na comunidade'
    }
  ];

  games = [
    { name: 'League of Legends', image: '🎮' },
    { name: 'Counter-Strike 2', image: '🔫' },
    { name: 'Valorant', image: '💥' },
    { name: 'Dota 2', image: '⚔️' },
    { name: 'Overwatch 2', image: '🦸' },
    { name: 'Rainbow Six', image: '🎯' }
  ];

  onGetStarted(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      this.router.navigate(['/tournaments']);
    } else {
      this.router.navigate(['/auth/register']);
    }
  }

  onViewTournaments(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      this.router.navigate(['/tournaments']);
    } else {
      this.router.navigate(['/auth/login']);
    }
  }

  scrollToFeatures(): void {
    document.getElementById('features')?.scrollIntoView({ behavior: 'smooth' });
  }
}
