import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {
  searchTerm: string = '';
  users: any[] = [];
  filteredUsers: any[] = [];
  showEditForm: boolean = false;
  editedUser: any = null;

  constructor(private authService: UserService) {}

  ngOnInit() {
    this.loadUsers();
  }
  

  // ✅ Récupérer tous les utilisateurs du backend
  loadUsers() {
    this.authService.getAllUsers().subscribe(
      (data: any[]) => {
        console.log('Réponse complète de l\'API des utilisateurs:', JSON.stringify(data, null, 2)); // Affichage détaillé de la réponse
  
        // Essayer de visualiser la structure de la réponse
        console.log('Structure de la première entrée dans la réponse API:', data[0]);
  
        // Vérification de la première entrée dans la réponse
        this.users = data.map(user => ({
          userId: user.userId,    // Assurez-vous que 'userId' est bien défini
          name: user.name,        // Assurez-vous que 'name' est bien défini
          email: user.email,      // Vérifiez 'email'
          phone: user.phone || '',// Vérifiez 'phone'
          date: user.date || '', // Vérifiez 'date'
          typeUser: user.typeUser, 
          createdAt: user.createdAt,
          updatedAt: user.updatedAt,
          status: user.status || 'ACTIVE'

        }));
  
        console.log('Utilisateurs récupérés:', this.users); // Log après mappage
        this.filteredUsers = [...this.users]; // Mise à jour de filteredUsers
      },
      (error: any) => {
        console.error('Erreur lors de la récupération des utilisateurs:', error);
      }
    );
  }
  
  filterUsers() {
    console.log('Recherche en cours avec le terme:', this.searchTerm); // Log du terme de recherche
    this.filteredUsers = this.users.filter(user =>
      user.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      user.email.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      user.phone.includes(this.searchTerm) ||
      user.date.includes(this.searchTerm) ||
      user.updatedAt.includes(this.searchTerm)
      
    );
    console.log('Utilisateurs filtrés:', this.filteredUsers); // Log de filteredUsers après filtrage
  }
  toggleBan(user: any) {
    const action = user.status === 'ACTIVE' ? this.authService.banUser(user.userId) : this.authService.unbanUser(user.userId);
  
    action.subscribe({
      next: () => {
        user.status = user.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
        alert(`✅ L'utilisateur ${user.name} est maintenant ${user.status === 'INACTIVE' ? 'banni' : 'actif'}.`);
      },
      error: (err) => {
        console.error('❌ Erreur lors du changement de statut:', err);
        alert('Erreur serveur lors du changement de statut');
      }
    });
  }
  
  

  deleteUser(userId: number) {
    if (confirm(`Êtes-vous sûr de vouloir bloquer l'utilisateur ID ${userId} ?`)) {
      this.authService.deleteUser(userId).subscribe(
        (response) => {
          // console.log("✅ Suppression réussie :", response);
          alert("Utilisateur bloqué avec succès !");
          this.users = this.users.filter(user => user.userId !== userId);
          this.filteredUsers = [...this.users]; // ✅ Mise à jour de la liste affichée
        },
        (error) => {
          // console.error("❌ Erreur lors de la suppression :", error);
          alert("⚠ Erreur lors du blocage de l'utilisateur !");
        }
      );
    }
  }
  

  // ✅ Modifier un utilisateur
  editUser(user: any) {
    this.editedUser = { ...user };
    this.showEditForm = true;
  }

  // ✅ Enregistrer les modifications
  saveChanges() {
    this.authService.updateUser(this.editedUser.userId, this.editedUser).subscribe(
      () => {
        const index = this.users.findIndex(u => u.userId === this.editedUser.userId);
        if (index !== -1) {
          this.users[index] = { ...this.editedUser, updatedAt: new Date().toISOString().split('T')[0] };
          this.filteredUsers = [...this.users];
        }
        this.showEditForm = false;
      },
      (error: any) => {
        console.error("Erreur :", error);
      }
      
    );
  }

  // ✅ Annuler la modification
  cancelEdit() {
    this.showEditForm = false;
  }
}
