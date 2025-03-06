import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from '../../components/navbar/navbar.component';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  standalone: true,
  imports:[ FormsModule,RouterModule,NavbarComponent]
})
export class RegisterComponent {
  name = '';
  email = '';
  password = '';
  phone = '';
  date =  '';

  constructor(private authService: AuthService, private router: Router) {}

  register() {
    const userData = {
      name: this.name,
      email: this.email,
      password: this.password,
      phone: this.phone,
      date: this.date, 
      typeUser: 'MEMBER' // Default type
    };
    this.authService.register(userData).subscribe(() => {
      this.router.navigate(['/login']);
    });
  }
}
