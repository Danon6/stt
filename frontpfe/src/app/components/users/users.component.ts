import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MainNavbarComponent } from '../main-navbar/main-navbar.component';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MainNavbarComponent
  ],
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss'
})
export class UsersComponent implements OnInit {
  members: any[] = [];
  filteredMembers: any[] = [];
  searchTerm: string = '';
  isLoading: boolean = true;
  error: string | null = null;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.fetchMembers();
  }

  fetchMembers() {
    this.isLoading = true;
    this.http.get<any[]>('http://localhost:8080/api/user/all').subscribe({
      next: (users) => {
        this.members = users.filter(user => user.typeUser === 'MEMBER');
        this.filteredMembers = [...this.members];
        this.isLoading = false;
      },
      error: (err) => {
        console.error('❌ Failed to load users:', err);
        this.error = 'Failed to load users. Please try again later.';
        this.isLoading = false;
      }
    });
  }

  searchUsers() {
    if (!this.searchTerm.trim()) {
      this.filteredMembers = [...this.members];
      return;
    }
    
    const term = this.searchTerm.toLowerCase().trim();
    this.filteredMembers = this.members.filter(member => 
      member.name?.toLowerCase().includes(term)
    );
  }

  getInitials(name: string): string {
    if (!name) return '?';
    return name.split(' ')
      .map(part => part.charAt(0))
      .slice(0, 2)
      .join('')
      .toUpperCase();
  }

  getRandomColor(name: string): string {
    const colors = [
      'bg-blue-500', 'bg-purple-500', 'bg-pink-500', 
      'bg-indigo-500', 'bg-green-500', 'bg-yellow-500',
      'bg-red-500', 'bg-teal-500'
    ];
    
    // Generate a consistent index based on the name
    const index = name?.split('').reduce((acc, char) => acc + char.charCodeAt(0), 0) % colors.length || 0;
    return colors[index];
  }
}