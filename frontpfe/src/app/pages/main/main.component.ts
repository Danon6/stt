import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from '../../components/navbar/navbar.component';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
  standalone: true,
  imports :[FormsModule,NavbarComponent]

})
export class MainComponent {
  constructor(private authService: AuthService) {}

  logout() {
    this.authService.logout();
  }
}
