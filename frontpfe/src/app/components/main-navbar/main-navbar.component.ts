import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common'; // ✅ Importer CommonModule
import { RouterModule } from '@angular/router'; 

@Component({
  selector: 'app-main-navbar',
  templateUrl: './main-navbar.component.html',
  styleUrls: ['./main-navbar.component.scss'],
  standalone: true,
  imports: [CommonModule , RouterModule] // ✅ Ajouter CommonModule ici
})
export class MainNavbarComponent implements OnInit {
  isAdmin: boolean = false;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit() {
    this.isAdmin = this.authService.isAdmin();
  }

  navigateTo(route: string) {
    this.router.navigate([route]);
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('typeUser');
    this.router.navigate(['/login']);
  }
}
