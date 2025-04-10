import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './components/navbar/navbar.component';

// Import the standalone MainComponent directly
import { MainComponent } from './pages/main/main.component';
import { TrendingComponent } from './pages/trending/trending.component';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  imports: [
    RouterModule,
    RouterOutlet,
    FormsModule,
    CommonModule,
    HttpClientModule,
    NavbarComponent,
    MainComponent,
    TrendingComponent
  ]
})
export class AppComponent {
  title = 'frontpfe';
}
