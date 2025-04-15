import { CommonModule } from '@angular/common';
import { Component, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink,CommonModule,FormsModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  constructor(
    private router: Router,
  ) {}
  onQuestionsClick(): void {
    console.log('Navigating to MainComponent...');
    this.router.navigate(['/main']);
  }
  onUsersClick(): void {
    console.log('Navigating to MainComponent...');
    this.router.navigate(['/users']);
  }
}
