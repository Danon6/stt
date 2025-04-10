import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { UserManagementComponent } from './user-management/user-management.component';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, NavbarComponent, UserManagementComponent],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent {
  showUserManagement = false; // Caché par défaut

  toggleUserManagement() {
    this.showUserManagement = !this.showUserManagement; // Change l'état à chaque clic
    console.log('toggleUserManagement appelé. showUserManagement:', this.showUserManagement); // Log l'état de showUserManagement
  }
}
