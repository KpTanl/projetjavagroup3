# Projet Java Group 3 – Backend Spring Boot

> 📘 Ce guide est rédigé pour les débutants. Si vous n'avez jamais utilisé Git ou Spring Boot, suivez simplement les étapes ci-dessous.

## 📋 Table des matières

1. [Prérequis](#1-prérequis)
2. [Installation avec IntelliJ IDEA](#2-installation-avec-intellij-idea-recommandé)
3. [Lancer le projet sans IDE](#3-lancer-le-projet-sans-ide-optionnel)
4. [Organisation du projet](#4-organisation-du-projet)
5. [Guide Git pour débutants](#5-guide-git-pour-débutants)
6. [Règles de branches (très important)](#6-règles-de-branches-très-important)
7. [Convention de commits](#7-convention-de-commits-obligatoire)
8. [Workflow complet pas à pas](#8-workflow-complet-pas-à-pas)
9. [Problèmes courants et solutions](#9-problèmes-courants-et-solutions)
10. [État du projet](#10-état-du-projet)

---

## 1. Prérequis

### Option recommandée : IntelliJ IDEA (le plus simple)

👉 **Installer uniquement [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download/)**

IntelliJ IDEA gère automatiquement :
- ✅ L'installation du JDK (Java 17)
- ✅ La configuration de Maven
- ✅ L'import du projet
- ✅ L'exécution du serveur

> 💡 **Conseil** : Si vous êtes débutant, utilisez IntelliJ IDEA. Vous n'aurez pratiquement rien à configurer manuellement.

---

## 2. Installation avec IntelliJ IDEA (recommandé)

### Étape 1 : Télécharger et installer IntelliJ IDEA

1. Télécharger : [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download/)
2. Installer en suivant l'assistant d'installation

### Étape 2 : Cloner le projet depuis GitHub

**Méthode A : Via IntelliJ IDEA (recommandé pour débutants)**

1. Lancer IntelliJ IDEA
2. Sur l'écran d'accueil, cliquer sur **Get from VCS**
3. Entrer l'URL du dépôt GitHub
4. Choisir le dossier de destination
5. Cliquer sur **Clone**

**Méthode B : Via terminal**

```bash
git clone <URL_DU_DEPOT>
cd projetjavagroup3
```

### Étape 3 : Ouvrir le projet

1. **File** → **Open** → Sélectionner le dossier du projet
2. IntelliJ détecte automatiquement le projet Maven et le fichier `pom.xml`

### Étape 4 : Installer Java 17 via IntelliJ

Si Java 17 n'est pas installé, IntelliJ vous proposera automatiquement :

1. Une notification apparaît : **Download JDK**
2. Cliquer dessus et choisir :
   - **Version** : `17`
   - **Distribution** : `Eclipse Temurin` (recommandé)
3. Cliquer sur **Download**

> ⚠️ **Important** : Le projet nécessite Java 17. Assurez-vous de sélectionner la version 17.

### Étape 5 : Synchroniser Maven

1. Clic droit sur `pom.xml` dans l'explorateur de fichiers
2. **Maven** → **Reload Project**

Ou bien cliquez sur l'icône 🔄 (Reload) dans la fenêtre Maven (à droite).

### Étape 6 : Lancer le projet

1. Ouvrir le fichier :
   ```
   src/main/java/com/example/projetjavagroup3/Projetjavagroup3Application.java
   ```
2. Cliquer sur ▶️ **Run** à côté de la méthode `main`

Si tout fonctionne, vous verrez dans la console :
```
Tomcat started on port 8080
Started Projetjavagroup3Application
```

🎉 **Le backend est accessible sur** : http://localhost:8080

---

## 3. Lancer le projet sans IDE (optionnel)

> ⚠️ Cette méthode demande plus de configuration manuelle.

### Prérequis

- Java 17 installé et configuré dans le PATH
- Maven (ou utiliser le wrapper Maven inclus)

### Commandes

**Sous Windows :**
```bash
mvnw.cmd spring-boot:run
```

**Sous Linux / macOS :**
```bash
./mvnw spring-boot:run
```

---

## 4. Organisation du projet

```
src/
 └─ main/
    ├─ java/
    │   └─ com.example.projetjavagroup3
    │       ├─ Projetjavagroup3Application.java  ← Point d'entrée
    │       ├─ controller/                        ← Contrôleurs REST
    │       ├─ service/                           ← Logique métier
    │       ├─ model/                             ← Entités / Modèles
    │       └─ repository/                        ← Accès aux données
    └─ resources/
        └─ application.properties                 ← Configuration
```

---

## 4.1 Spring Data JPA et SQLite

> Ce projet utilise **Spring Data JPA** avec une base de données **SQLite**.

### Configuration de la base de données

La configuration se trouve dans `src/main/resources/application.properties` :

```properties
# SQLite
spring.datasource.url=jdbc:sqlite:database.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
```

### Comment créer une entité (Entity)

Une entité représente une table dans la base de données.

```java
package com.example.projetjavagroup3.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data  // Lombok génère getters/setters automatiquement
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
}
```

### Comment créer un Repository

Le Repository fournit automatiquement les opérations CRUD.

```java
package com.example.projetjavagroup3.repository;

import com.example.projetjavagroup3.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Méthodes CRUD automatiques :
    // - save(User user)           → Créer ou mettre à jour
    // - findById(Long id)         → Trouver par ID
    // - findAll()                 → Récupérer tous
    // - deleteById(Long id)       → Supprimer par ID
    
    // Méthodes personnalisées (Spring génère l'implémentation) :
    List<User> findByName(String name);
    List<User> findByEmailContaining(String keyword);
}
```

### Comment utiliser dans un Service

```java
package com.example.projetjavagroup3.service;

import com.example.projetjavagroup3.model.User;
import com.example.projetjavagroup3.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
```

### Comment créer un Controller REST

```java
package com.example.projetjavagroup3.controller;

import com.example.projetjavagroup3.model.User;
import com.example.projetjavagroup3.service.UserService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
}
```

### Résumé des annotations importantes

| Annotation | Description |
|------------|-------------|
| `@Entity` | Marque une classe comme entité JPA (table) |
| `@Id` | Clé primaire |
| `@GeneratedValue` | ID auto-généré |
| `@Repository` | Interface d'accès aux données |
| `@Service` | Classe de logique métier |
| `@RestController` | Contrôleur REST API |
| `@GetMapping` | Requête HTTP GET |
| `@PostMapping` | Requête HTTP POST |
| `@RequestBody` | Corps de la requête JSON → Objet |
| `@PathVariable` | Paramètre dans l'URL |

---

## 5. Guide Git pour débutants

> 🆕 **Nouveau sur Git ?** Cette section vous explique les commandes essentielles.

### Qu'est-ce que Git ?

Git est un système de contrôle de version. Il permet à plusieurs personnes de travailler sur le même projet sans se marcher sur les pieds.

### Concepts de base

| Terme | Explication |
|-------|-------------|
| **Repository (dépôt)** | Le dossier du projet géré par Git |
| **Commit** | Une "sauvegarde" de vos modifications |
| **Branch (branche)** | Une copie parallèle du projet pour travailler isolément |
| **Merge** | Fusionner une branche dans une autre |
| **Pull** | Récupérer les dernières modifications depuis GitHub |
| **Push** | Envoyer vos modifications vers GitHub |

### Commandes Git essentielles

#### 📥 Récupérer les dernières modifications
```bash
git pull origin main
```
**Quand l'utiliser ?** Avant de commencer à travailler chaque jour.

#### 📂 Voir l'état de vos fichiers
```bash
git status
```
**Quand l'utiliser ?** Pour voir quels fichiers ont été modifiés.

#### ➕ Ajouter des fichiers à commit
```bash
git add .
```
**Quand l'utiliser ?** Après avoir modifié des fichiers et avant de commit.

#### 💾 Créer un commit (sauvegarder)
```bash
git commit -m "feat: description claire de ma modification"
```
**Quand l'utiliser ?** Après avoir ajouté les fichiers avec `git add`.

#### 📤 Envoyer sur GitHub
```bash
git push origin nom-de-ma-branche
```
**Quand l'utiliser ?** Pour partager votre travail avec l'équipe.

---

## 6. Règles de branches (très important)

### ⛔ Règle n°1 : Ne jamais travailler directement sur `main`

> La branche `main` doit **toujours rester stable et fonctionnelle**.
> Elle sert de référence pour tout le monde.

### ✅ Chaque développeur travaille sur sa propre branche

Avant de coder, créez une branche dédiée à votre fonctionnalité.

### Nommage des branches

Utilisez le format suivant :

| Type | Format | Exemple |
|------|--------|---------|
| Nouvelle fonctionnalité | `feature/nom-fonctionnalite` | `feature/user-controller` |
| Correction de bug | `fix/nom-bug` | `fix/login-error` |
| Tests | `test/nom-test` | `test/user-service-tests` |
| Documentation | `docs/nom-doc` | `docs/readme-update` |

### Comment créer une branche ?

```bash
# 1. Se placer sur main et récupérer les dernières modifications
git checkout main
git pull origin main

# 2. Créer votre branche et vous y déplacer
git checkout -b feature/ma-fonctionnalite
```

### Comment changer de branche ?

```bash
# Voir toutes les branches
git branch -a

# Changer de branche
git checkout nom-de-la-branche
```

### Comment supprimer une branche ?

```bash
# Supprimer une branche locale (après merge)
git branch -d feature/ma-fonctionnalite
```

---

## 7. Convention de commits (obligatoire)

Pour garder un historique clair et lisible, **chaque commit doit suivre ce format** :

```
type: description courte et claire
```

### Types de commits

| Type | Description | Exemple |
|------|-------------|---------|
| `feat` | Nouvelle fonctionnalité | `feat: add user registration endpoint` |
| `fix` | Correction de bug | `fix: resolve null pointer in login` |
| `test` | Ajout/modification de tests | `test: add user service unit tests` |
| `docs` | Documentation | `docs: update README setup guide` |
| `refactor` | Amélioration du code | `refactor: simplify user validation logic` |

### ❌ Messages à éviter absolument

- `update`
- `fix`
- `test`
- `wip`
- `first commit`
- `ok`
- `changes`

### ✅ Bons exemples

```bash
git commit -m "feat: add GET /users endpoint"
git commit -m "fix: correct database connection timeout"
git commit -m "docs: add Git workflow instructions"
git commit -m "refactor: extract validation to separate method"
```

---

## 8. Workflow complet pas à pas

> 📋 Suivez ces étapes à chaque fois que vous travaillez sur le projet.

### Avant de commencer à coder

```bash
# 1. Se placer sur main
git checkout main

# 2. Récupérer les dernières modifications
git pull origin main

# 3. Créer une nouvelle branche pour votre travail
git checkout -b feature/nom-de-ma-feature
```

### Pendant le développement

```bash
# 4. Coder vos modifications...

# 5. Vérifier les fichiers modifiés
git status

# 6. Ajouter les fichiers
git add .

# 7. Créer un commit
git commit -m "feat: description de ma modification"
```

### Après avoir terminé

```bash
# 8. Pousser votre branche sur GitHub
git push origin feature/nom-de-ma-feature
```

### Fusionner dans main (via Pull Request)

1. Aller sur GitHub
2. Vous verrez un message proposant de créer une **Pull Request**
3. Cliquer sur **Compare & pull request**
4. Ajouter une description
5. Demander une revue à un membre de l'équipe
6. Une fois approuvée, cliquer sur **Merge pull request**

> 💡 **Ne jamais merger directement sans Pull Request**. Cela permet à l'équipe de relire le code.

---

## 9. Problèmes courants et solutions

### ❌ Erreur : "Your branch is behind 'origin/main'"

**Solution :**
```bash
git checkout main
git pull origin main
git checkout ma-branche
git merge main
```

### ❌ Erreur : "CONFLICT (content)"

**Solution :**
1. Ouvrir le fichier en conflit dans IntelliJ
2. IntelliJ affiche une interface pour résoudre le conflit
3. Choisir les modifications à garder
4. Après résolution :
```bash
git add .
git commit -m "fix: resolve merge conflicts"
```

### ❌ Erreur : "Permission denied" lors du push

**Solution :**
- Vérifier que vous avez accès au dépôt GitHub
- Vérifier votre configuration SSH ou HTTPS

### ❌ Le projet ne démarre pas

**Vérifications :**
1. Java 17 est-il bien installé ? (Project Structure → SDK)
2. Maven est-il synchronisé ? (Maven → Reload Project)
3. Le port 8080 est-il déjà utilisé ?

---

## 10. État du projet

| Élément | Statut |
|---------|--------|
| Projet Spring Boot | ✅ Initialisé |
| Java 17 | ✅ Configuré |
| Maven | ✅ Synchronisé |
| Tests | ✅ Prêt |
| Développement métier | 🔄 En cours |

---

## 📞 Besoin d'aide ?

- Demandez de l'aide sur le groupe de discussion de l'équipe
- Consultez la [documentation Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- N'hésitez pas à poser des questions avant de modifier des fichiers de configuration

---

> ⚠️ **Rappel final** : Toujours `git pull` avant de travailler, toujours créer une branche, toujours suivre la convention de commits !
