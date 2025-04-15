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
  showUserManagement = true;
  showQuestionsManagement = false;
  showKnowledgesManagement = false;

  showSection(section: 'users' | 'questions' | 'knowledge') {
    this.showUserManagement = section === 'users';
    this.showQuestionsManagement = section === 'questions';
    this.showKnowledgesManagement = section === 'knowledge';
  }

  getBtnClass(active: boolean): string {
    return active
      ? 'bg-indigo-600 text-white font-semibold'
      : 'text-gray-700 bg-gray-100';
  }
}

