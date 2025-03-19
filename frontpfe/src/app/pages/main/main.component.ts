import { Component } from '@angular/core';
import { MainNavbarComponent } from '../../components/main-navbar/main-navbar.component';
import { CommonModule } from '@angular/common'; // ✅ Importer CommonModule

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
  standalone: true,
  imports: [CommonModule, MainNavbarComponent] // ✅ Ajouter CommonModule pour `*ngIf`
})
export class MainComponent { }
