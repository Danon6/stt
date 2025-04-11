import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { MainComponent } from './pages/main/main.component';
import { AdminDashboardComponent } from './pages/admin-dashboard/admin-dashboard.component';
import { UserManagementComponent } from './pages/admin-dashboard/user-management/user-management.component'; // ✅ Import User Management
import { AuthGuard } from './guards/auth.guard';
import { TrendingComponent } from './pages/trending/trending.component';
import { UsersComponent } from './components/users/users.component';
import { ProfileComponent } from './components/profile/profile.component';


export const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'main', component: MainComponent},
    { path: 'trending', component: TrendingComponent },
    {path:'users',component:UsersComponent},
    {path:'me',component:ProfileComponent},

    // Dashboard Admin avec User Management
    { 
      path: 'admin', 
      component: AdminDashboardComponent, 
      canActivate: [AuthGuard],
      children: [
          { path: '', redirectTo: 'users', pathMatch: 'full' }, // ✅ Redirection vers /admin/users
          { path: 'users', component: UserManagementComponent } // ✅ Page User Management
          
      ]
    },

    { path: '**', redirectTo: 'login' } 
];
