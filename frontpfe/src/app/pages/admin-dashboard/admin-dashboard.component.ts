import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { QuestionsManagementComponent } from './questions-management/questions-management.component';
import { KnowledgesManagementComponent } from './knowledges-management/knowledges-management.component';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, NavbarComponent, UserManagementComponent,QuestionsManagementComponent,KnowledgesManagementComponent],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent {
  showUserManagement = false; // Caché par défaut
  showQuestionsManagement = false;
  showKnowledgesManagement = false;
  toggleUserManagement() {
    this.showUserManagement = !this.showUserManagement; // Change l'état à chaque clic
    console.log('toggleUserManagement appelé. showUserManagement:', this.showUserManagement); // Log l'état de showUserManagement
  }
  toggleQuestionsManagement() {
    this.showQuestionsManagement = !this.showQuestionsManagement; // Change l'état à chaque clic
    console.log('togglQuestionsManagement appelé. showQuestionsManagement:', this.showQuestionsManagement); // Log l'état de showUserManagement
  }
  toggleKnowledgesManagement() {
    this.showKnowledgesManagement = !this.showKnowledgesManagement; // Change l'état à chaque clic
    console.log('toggleKnowledgesManagement appelé. showKnowledgesManagement:', this.showKnowledgesManagement); // Log l'état de showUserManagement
  }
}
