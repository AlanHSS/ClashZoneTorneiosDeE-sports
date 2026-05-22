import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { UserService } from '@core/services/user';
import { AuthService } from '@core/services/auth';
import { ToastService } from '@core/services/toast';
import { AtualizarUsuariosDto, UsuariosDomain } from '@core/models';

@Component({
  selector: 'app-user-profile-edit',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './user-profile-edit.html',
  styleUrl: './user-profile-edit.scss'
})
export class UserProfileEditComponent implements OnInit {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private userService = inject(UserService);
  private authService = inject(AuthService);
  private toastService = inject(ToastService);

  user: UsuariosDomain | null = null;
  loading = true;
  saving = false;
  deleting = false;

  profileForm: FormGroup = this.fb.group({
    nickname: [{ value: '', disabled: true }],
    nomeDoUsuario: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    emailDoUsuario: ['', [Validators.required, Validators.email, Validators.maxLength(255)]],
    senhaDoUsuario: ['']
  });

  ngOnInit(): void {
    const current = this.authService.getCurrentUser();
    if (!current?.id) {
      this.router.navigate(['/auth/login']);
      return;
    }

    this.loadProfile(current.id);
  }

  private loadProfile(id: number): void {
    this.loading = true;
    this.userService.buscarPorId(id).subscribe({
      next: (data) => {
        this.user = data;
        this.profileForm.patchValue({
          nickname: data.nickname,
          nomeDoUsuario: data.nomeDoUsuario,
          emailDoUsuario: data.emailDoUsuario
        });
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.toastService.error('Erro ao carregar perfil');
        this.router.navigate(['/']);
      }
    });
  }

  onSave(): void {
    if (!this.user) return;
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }

    const raw = this.profileForm.getRawValue();

    const dto: AtualizarUsuariosDto = {};
    if (raw.nomeDoUsuario && raw.nomeDoUsuario !== this.user.nomeDoUsuario) dto.nomeDoUsuario = raw.nomeDoUsuario;
    if (raw.emailDoUsuario && raw.emailDoUsuario !== this.user.emailDoUsuario) dto.emailDoUsuario = raw.emailDoUsuario;
    if (raw.senhaDoUsuario && String(raw.senhaDoUsuario).trim() !== '') dto.senhaDoUsuario = raw.senhaDoUsuario;

    if (Object.keys(dto).length === 0) {
      this.toastService.info('Nenhuma alteração para salvar');
      return;
    }

    this.saving = true;
    this.userService.atualizarPerfil(this.user.id, dto).subscribe({
      next: (updated) => {
        this.user = { ...this.user!, ...updated };
        this.profileForm.patchValue({
          nomeDoUsuario: this.user.nomeDoUsuario,
          emailDoUsuario: this.user.emailDoUsuario,
          senhaDoUsuario: ''
        });

        this.authService.updateCurrentUser({
          nomeDoUsuario: this.user.nomeDoUsuario,
          emailDoUsuario: this.user.emailDoUsuario
        });

        this.toastService.success('Perfil atualizado com sucesso!');
        this.saving = false;
        this.router.navigate(['/profile']);
      },
      error: () => {
        this.saving = false;
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/profile']);
  }

  onDeleteAccount(): void {
    if (!this.user) return;

    if (!confirm('Tem certeza que deseja excluir sua conta? Esta ação não pode ser desfeita.')) {
      return;
    }

    this.deleting = true;
    this.userService.deletarConta(this.user.id).subscribe({
      next: () => {
        this.deleting = false;
        this.toastService.success('Conta excluída com sucesso!');
        this.authService.logout();
      },
      error: () => {
        this.deleting = false;
      }
    });
  }
}
