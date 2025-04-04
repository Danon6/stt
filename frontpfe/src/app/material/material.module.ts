// src/app/material/material.module.ts
import { NgModule } from '@angular/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  exports: [
    MatProgressSpinnerModule,  // Corrected MatSpinnerModule to MatProgressSpinnerModule
    MatFormFieldModule,
    MatInputModule,
    MatChipsModule,  // Corrected MatChipInputModule to MatChipsModule
    MatIconModule
  ]
})
export class MaterialModule {}
