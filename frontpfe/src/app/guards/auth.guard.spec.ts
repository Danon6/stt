import { TestBed } from '@angular/core/testing';
import { AuthGuard } from './auth.guard';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { of } from 'rxjs';

describe('AuthGuard', () => {
  let authGuard: AuthGuard;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(() => {
    // Création de mock (espions Jasmine) pour AuthService et Router
    authService = jasmine.createSpyObj<AuthService>('AuthService', ['isAuthenticated']);
    router = jasmine.createSpyObj<Router>('Router', ['navigate']);

    TestBed.configureTestingModule({
      providers: [
        AuthGuard,
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router }
      ]
    });

    authGuard = TestBed.inject(AuthGuard);
  });

  it('should allow access if user is authenticated', () => {
    authService.isAuthenticated.and.returnValue(true); // Simule un utilisateur connecté
    expect(authGuard.canActivate()).toBeTrue();
  });

  it('should deny access and redirect if user is not authenticated', () => {
    authService.isAuthenticated.and.returnValue(false); // Simule un utilisateur non connecté
    expect(authGuard.canActivate()).toBeFalse();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });
});
