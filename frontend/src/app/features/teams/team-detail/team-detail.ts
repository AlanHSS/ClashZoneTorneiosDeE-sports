import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { TeamService } from '@core/services/team';
import { ToastService } from '@core/services/toast';
import { AuthService } from '@core/services/auth';
import { EquipeDomain, MembroEquipeDomain, TipoMembro } from '@core/models';
import { GameNamePipe } from '@shared/pipes/game-name-pipe';
import { MemberFormDialogComponent } from '../member-form-dialog/member-form-dialog';

@Component({
  selector: 'app-team-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatDialogModule,
    GameNamePipe
  ],
  templateUrl: './team-detail.html',
  styleUrl: './team-detail.scss'
})
export class TeamDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private teamService = inject(TeamService);
  private toastService = inject(ToastService);
  private authService = inject(AuthService);
  private dialog = inject(MatDialog);

  team: EquipeDomain | null = null;
  members: MembroEquipeDomain[] = [];
  loading = true;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadTeam(id);
      this.loadMembers(id);
    }
  }

  loadTeam(id: number): void {
    this.teamService.buscarPorId(id).subscribe({
      next: (data) => {
        this.team = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.router.navigate(['/teams']);
      }
    });
  }

  loadMembers(equipeId: number): void {
    this.teamService.listarMembros(equipeId).subscribe({
      next: (data) => {
        this.members = data;
      },
      error: () => {
        this.members = [];
      }
    });
  }

  onAddMember(): void {
    if (!this.team) return;

    const dialogRef = this.dialog.open(MemberFormDialogComponent, {
      width: '500px',
      data: { equipeId: this.team.id }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadMembers(this.team!.id);
        this.loadTeam(this.team!.id);
      }
    });
  }

  onToggleStatus(member: MembroEquipeDomain): void {
    if (!this.team) return;

    const novoTipo: TipoMembro = member.tipo === TipoMembro.TITULAR
      ? TipoMembro.RESERVA
      : TipoMembro.TITULAR;

    const updateDto = {
      id: member.id,
      tipo: novoTipo
    };

    this.teamService.atualizarMembro(this.team.id, updateDto).subscribe({
      next: () => {
        this.toastService.success(
          `${member.nickname} agora é ${novoTipo.toLowerCase()}`
        );
        this.loadMembers(this.team!.id);
      }
    });
  }

  onRemoveMember(member: MembroEquipeDomain): void {
    if (!this.team) return;

    if (confirm(`Tem certeza que deseja remover ${member.nickname} da equipe?`)) {
      this.teamService.removerMembro(this.team.id, member.id).subscribe({
        next: () => {
          this.toastService.success('Membro removido com sucesso!');
          this.loadMembers(this.team!.id);
          this.loadTeam(this.team!.id);
        }
      });
    }
  }

  onEdit(): void {
    if (this.team) {
      this.router.navigate(['/teams/edit', this.team.id]);
    }
  }

  onDelete(): void {
    if (!this.team) return;

    if (confirm(`Tem certeza que deseja excluir a equipe "${this.team.nomeDaEquipe}"?`)) {
      this.teamService.deletar(this.team.id).subscribe({
        next: () => {
          this.toastService.success('Equipe excluída com sucesso!');
          this.router.navigate(['/teams']);
        }
      });
    }
  }

  onBack(): void {
    this.router.navigate(['/teams']);
  }

  canManage(): boolean {
    if (!this.team) return false;
    const currentUser = this.authService.getCurrentUser();
    return currentUser?.id === this.team.liderId || this.authService.isAdmin();
  }

  getTotalMembers(): number {
    return this.members.length;
  }

  getTitulares(): number {
    return this.members.filter(m => m.tipo === TipoMembro.TITULAR).length;
  }

  getReservas(): number {
    return this.members.filter(m => m.tipo === TipoMembro.RESERVA).length;
  }

  getTitularesList(): MembroEquipeDomain[] {
    return this.members.filter(m => m.tipo === TipoMembro.TITULAR);
  }

  getReservasList(): MembroEquipeDomain[] {
    return this.members.filter(m => m.tipo === TipoMembro.RESERVA);
  }
}
