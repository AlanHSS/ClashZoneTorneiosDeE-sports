import { Component, Inject, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { TeamService } from '@core/services/team';
import { NotificationService } from '@core/services/notification';
import { TipoMembro } from '@core/models';

export interface MemberDialogData {
  equipeId: number;
}

@Component({
  selector: 'app-member-form-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './member-form-dialog.html',
  styleUrl: './member-form-dialog.scss'
})
export class MemberFormDialogComponent {
  private fb = inject(FormBuilder);
  private teamService = inject(TeamService);
  private notificationService = inject(NotificationService);
  private dialogRef = inject(MatDialogRef<MemberFormDialogComponent>);

  memberForm: FormGroup;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: MemberDialogData
  ) {
    this.memberForm = this.fb.group({
      nickname: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      rank: ['', [Validators.maxLength(50)]],
      tipo: [TipoMembro.TITULAR, Validators.required]
    });
  }

  onSubmit(): void {
    if (this.memberForm.valid) {
      const memberData = {
        nickname: this.memberForm.value.nickname,
        tipo: this.memberForm.value.tipo as TipoMembro,
        rank: this.memberForm.value.rank || undefined
      };

      this.teamService.adicionarMembro(this.data.equipeId, memberData).subscribe({
        next: () => {
          this.notificationService.success('Membro adicionado com sucesso!');
          this.dialogRef.close(true);
        }
      });
    } else {
      this.memberForm.markAllAsTouched();
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  getErrorMessage(field: string): string {
    const control = this.memberForm.get(field);

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
