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
import { TeamService } from '@core/services/team';
import { ToastService } from '@core/services/toast';
import { Games } from '@core/models';
import { GameNamePipe } from '@shared/pipes/game-name-pipe';

@Component({
  selector: 'app-team-form',
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
    GameNamePipe
  ],
  templateUrl: './team-form.html',
  styleUrl: './team-form.scss'
})
export class TeamFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private teamService = inject(TeamService);
  private toastService = inject(ToastService);

  teamForm: FormGroup;
  isEditMode = false;
  teamId: number | null = null;

  games = Object.values(Games);

  constructor() {
    this.teamForm = this.fb.group({
      nomeDaEquipe: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      jogo: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.teamId = Number(id);
      this.loadTeam(this.teamId);
    }
  }

  loadTeam(id: number): void {
    this.teamService.buscarPorId(id).subscribe({
      next: (data) => {
        this.teamForm.patchValue({
          nomeDaEquipe: data.nomeDaEquipe,
          jogo: data.jogo
        });
      },
      error: () => {
        this.toastService.error('Erro ao carregar equipe');
        this.router.navigate(['/teams']);
      }
    });
  }

  onSubmit(): void {
    if (this.teamForm.valid) {
      const formData = {
        nomeDaEquipe: this.teamForm.value.nomeDaEquipe,
        jogo: this.teamForm.value.jogo
      };

      if (this.isEditMode && this.teamId) {
        this.updateTeam(this.teamId, formData);
      } else {
        this.createTeam(formData);
      }
    } else {
      this.teamForm.markAllAsTouched();
    }
  }

  createTeam(data: any): void {
    this.teamService.criar(data).subscribe({
      next: (response) => {
        this.toastService.success('Equipe criada com sucesso!');
        if (response.id) {
          this.router.navigate(['/teams', response.id]);
        } else {
          this.router.navigate(['/teams']);
        }
      }
    });
  }

  updateTeam(id: number, data: any): void {
    this.teamService.atualizar(id, data).subscribe({
      next: () => {
        this.toastService.success('Equipe atualizada com sucesso!');
        this.router.navigate(['/teams', id]);
      }
    });
  }

  onCancel(): void {
    if (this.isEditMode && this.teamId) {
      this.router.navigate(['/teams', this.teamId]);
    } else {
      this.router.navigate(['/teams']);
    }
  }

  getErrorMessage(field: string): string {
    const control = this.teamForm.get(field);

    if (control?.hasError('required')) {
      return 'Este campo é obrigatório';
    }

    if (control?.hasError('minlength')) {
      const minLength = control.errors?.['minlength'].requiredLength;
      return `Mínimo de ${minLength} caracteres`;
    }

    if (control?.hasError('maxlength')) {
      const maxLength = control.errors?.['maxlength'].requiredLength;
      return `Máximo de ${maxLength} caracteres`;
    }

    return '';
  }
}
