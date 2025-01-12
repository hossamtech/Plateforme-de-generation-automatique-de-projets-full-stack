# Plateforme de Génération Automatique de Projets Full-Stack

## Description

Cette plateforme permet de **générer automatiquement des projets full-stack** à partir d’un **diagramme de classes UML** conçu par l’utilisateur. Elle simplifie le processus de développement en générant automatiquement le code backend, frontend, et les configurations de base de données. Les utilisateurs peuvent s'inscrire, se connecter, partager des liens, consulter l'historique des projets générés, et télécharger leurs projets finalisés.

---

## Fonctionnalités Principales

<table>
  <thead>
    <tr>
      <th>Fonctionnalité</th>
      <th>Description</th>
      <th>Aperçu</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><b>Sign In</b></td>
      <td>Permet aux utilisateurs de se connecter à leur compte pour accéder à leurs projets enregistrés.</td>
      <td><img src="/screenschot_app/sign-in.jpg" alt="Sign In" width="300"></td>
    </tr>
    <tr>
      <td><b>Sign Up</b></td>
      <td>Inscription pour les nouveaux utilisateurs en créant un compte personnel sécurisé.</td>
      <td><img src="/screenschot_app/sign-up.jpg" alt="Sign Up" width="300"></td>
    </tr>
    <tr>
      <td><b>Home</b></td>
      <td>
        L'interface principale offre un espace centralisé pour gérer les projets. Elle comprend une 
        <b>sidebar</b> avec un éditeur de code intégré permettant de définir les tables et relations 
        nécessaires, ainsi qu'une zone d'outils UX pour sauvegarder, partager et générer des projets. 
        Un <b>canvas interactif</b> affiche dynamiquement un diagramme UML généré à partir du code, 
        offrant une visualisation claire des entités et relations définies par l'utilisateur.
      </td>
      <td><img src="/screenschot_app/home.jpg" alt="Home" width="300"></td>
    </tr>
    <tr>
      <td><b>Share Link</b></td>
      <td>Partager des liens vers des projets ou des diagrammes UML avec d'autres utilisateurs.</td>
      <td><img src="/screenschot_app/share-link.jpg" alt="Share Link" width="300"></td>
    </tr>
    <tr>
      <td><b>Histories</b></td>
      <td>Afficher l'historique des projets générés précédemment par l'utilisateur.</td>
      <td><img src="/screenschot_app/histories.jpg" alt="Histories" width="300"></td>
    </tr>
    <tr>
      <td><b>Database Configuration</b></td>
      <td>Configurer la base de données et générer les scripts nécessaires (MySQL, PostgreSQL, MongoDB).</td>
      <td><img src="/screenschot_app/dataBase-config.jpg" alt="Database Configuration" width="300"></td>
    </tr>
    <tr>
      <td><b>Downloads Projects</b></td>
      <td>Télécharger le projet généré sous forme d'archive contenant le code source complet.</td>
      <td><img src="/screenschot_app/downloads-projects.jpg" alt="Downloads Projects" width="300"></td>
    </tr>
  </tbody>
</table>

---

## Technologies Utilisées

- **Backend** : Spring Boot
- **Frontend** : Angular 
- **Base de Données** : MySQL
