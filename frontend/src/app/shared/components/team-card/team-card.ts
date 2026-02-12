import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { EquipeDomain } from '@core/models';
import { GameNamePipe } from '@shared/pipes/game-name.pipe';

@Component({
  selector: 'app-team-card',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    GameNamePipe
  ],
  templateUrl: './team-card.html',
  styleUrl: './team-card.scss'
})
export class TeamCardComponent {
  @Input({ required: true }) team!: EquipeDomain;
  @Input() isOwner = false;
  @Output() viewDetails = new EventEmitter<number>();
  @Output() edit = new EventEmitter<number>();
  @Output() delete = new EventEmitter<number>();

  onViewDetails(): void {
    this.viewDetails.emit(this.team.id);
  }

  onEdit(): void {
    this.edit.emit(this.team.id);
  }

  onDelete(): void {
    this.delete.emit(this.team.id);
  }
}
