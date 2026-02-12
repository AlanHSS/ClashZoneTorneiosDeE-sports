import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { TorneioDomain } from '@core/models';
import { GameNamePipe } from '@shared/pipes/game-name.pipe';
import { StatusBadgePipe } from '@shared/pipes/status-badge.pipe';

@Component({
  selector: 'app-tournament-card',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatChipsModule,
    MatIconModule,
    GameNamePipe,
    StatusBadgePipe
  ],
  templateUrl: './tournament-card.html',
  styleUrl: './tournament-card.scss'
})
export class TournamentCardComponent {
  @Input({ required: true }) tournament!: TorneioDomain;
  @Output() viewDetails = new EventEmitter<number>();
  @Output() register = new EventEmitter<number>();

  onViewDetails(): void {
    this.viewDetails.emit(this.tournament.id);
  }

  onRegister(): void {
    this.register.emit(this.tournament.id);
  }
}
