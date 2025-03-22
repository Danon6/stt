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
        this.users = data.map(user => ({
          userId: user.userId,
          name: user.name,
          email: user.email,
          phone: user.phone || '',
          dateNaissance: user.dateNaissance || '',
          typeUser: user.typeUser,
          createdAt: user.createdAt,
          updatedAt: user.updatedAt
        }));
        this.filteredUsers = [...this.users]; // Mise à jour de l'affichage
      },
      (error: any) => {
        console.error("Erreur :", error);
      }
      
    );
  }

  // ✅ Filtrer les utilisateurs
  filterUsers() {
    this.filteredUsers = this.users.filter(user =>
      user.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      user.email.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      user.phone.includes(this.searchTerm) ||
      user.dateNaissance.includes(this.searchTerm) ||
      user.updatedAt.includes(this.searchTerm)
    );
  }

  deleteUser(userId: number) {
    if (confirm(`Êtes-vous sûr de vouloir supprimer l'utilisateur ID ${userId} ?`)) {
      this.authService.deleteUser(userId).subscribe(
        (response) => {
          console.log("✅ Suppression réussie :", response);
          alert("Utilisateur supprimé avec succès !");
          this.users = this.users.filter(user => user.userId !== userId);
          this.filteredUsers = [...this.users]; // ✅ Mise à jour de la liste affichée
        },
        (error) => {
          console.error("❌ Erreur lors de la suppression :", error);
          alert("⚠ Erreur lors de la suppression de l'utilisateur !");
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
