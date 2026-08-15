# 🏠 NovaImmo

## Plateforme moderne de gestion immobilière

**NovaImmo** est une application web de gestion immobilière conçue pour permettre à une agence immobilière de présenter ses biens, gérer ses clients, organiser les visites et rendez-vous, suivre les transactions immobilières, enregistrer les paiements et gérer les documents associés.

Le projet est conçu avec une architecture évolutive permettant de partir d'un site vitrine immobilier moderne pour évoluer progressivement vers une véritable plateforme de gestion immobilière.

Le siège social de NovaImmo est situé à **Jacmel, Haïti**, avec une orientation vers le développement du marché immobilier local et la création d'opportunités pour les propriétaires, acheteurs, investisseurs et partenaires.

---

# 📑 Table des matières

1. [Présentation](#-présentation)
2. [Objectifs](#-objectifs)
3. [Fonctionnalités](#-fonctionnalités)
4. [Architecture générale](#-architecture-générale)
5. [Technologies utilisées](#-technologies-utilisées)
6. [Structure du projet](#-structure-du-projet)
7. [Modèle métier](#-modèle-métier)
8. [Gestion des utilisateurs](#-gestion-des-utilisateurs)
9. [Authentification et sécurité](#-authentification-et-sécurité)
10. [Gestion des propriétés](#-gestion-des-propriétés)
11. [Gestion des visites](#-gestion-des-visites)
12. [Gestion des rendez-vous](#-gestion-des-rendez-vous)
13. [Gestion des transactions](#-gestion-des-transactions)
14. [Gestion des paiements](#-gestion-des-paiements)
15. [Gestion documentaire](#-gestion-documentaire)
16. [Gestion des projets](#-gestion-des-projets)
17. [Gestion des partenaires](#-gestion-des-partenaires)
18. [API REST](#-api-rest)
19. [Base de données](#-base-de-données)
20. [Installation](#-installation)
21. [Configuration](#-configuration)
22. [Démarrage](#-démarrage)
23. [Tests de l'API](#-tests-de-lapi)
24. [Sécurité des endpoints](#-sécurité-des-endpoints)
25. [Évolution prévue](#-évolution-prévue)

---

# 🎯 Présentation

NovaImmo répond à plusieurs besoins d'une entreprise immobilière moderne :

- présenter les propriétés disponibles ;
- permettre aux visiteurs de rechercher des biens ;
- recevoir des demandes de contact ;
- organiser des visites ;
- gérer des rendez-vous ;
- gérer les clients et agents ;
- suivre les transactions ;
- enregistrer les paiements ;
- conserver les documents liés aux transactions ;
- présenter des projets immobiliers ;
- gérer des partenaires et investisseurs.

L'application est pensée pour séparer clairement :

```text
Site public
     │
     ▼
API REST NovaImmo
     │
     ▼
Services métier
     │
     ▼
Spring Data JPA
     │
     ▼
Base de données MySQL
```

Cette organisation permettra ultérieurement d'utiliser la même API avec plusieurs interfaces :

- site web ;
- dashboard administratif ;
- application mobile ;
- application partenaire ;
- outils internes.

---

# 🚀 Objectifs

L'objectif principal est de construire progressivement une plateforme immobilière complète, sécurisée et évolutive.

NovaImmo doit permettre de gérer le cycle suivant :

```text
Publication d'une propriété
          │
          ▼
Consultation par un visiteur
          │
          ▼
Demande d'information
          │
          ▼
Demande de visite
          │
          ▼
Rendez-vous
          │
          ▼
Client intéressé
          │
          ▼
Négociation
          │
          ▼
Transaction
          │
          ▼
Paiements
          │
          ▼
Documents
          │
          ▼
Vente / Location terminée
```

---

# ⚙️ Fonctionnalités

## Site public

Le visiteur peut :

- consulter les propriétés ;
- consulter les détails d'une propriété ;
- découvrir les projets immobiliers ;
- envoyer une demande de contact ;
- demander une visite ;
- demander un rendez-vous.

## Client

Un client authentifié peut notamment :

- se connecter ;
- consulter ses visites ;
- consulter ses rendez-vous ;
- consulter ses transactions ;
- consulter ses paiements ;
- consulter les documents associés à ses transactions.

## Agent immobilier

Un agent peut notamment :

- gérer les propriétés ;
- traiter les contacts ;
- organiser les visites ;
- gérer les rendez-vous ;
- gérer les transactions ;
- enregistrer les paiements ;
- ajouter des documents aux transactions.

## Administrateur

L'administrateur possède les droits les plus élevés.

Il peut notamment :

- administrer les utilisateurs ;
- gérer les agents ;
- gérer les propriétés ;
- gérer les projets ;
- gérer les partenaires ;
- superviser les transactions ;
- superviser les paiements ;
- gérer les documents ;
- accéder aux fonctions administratives.

---

# 🏗️ Architecture générale

Le backend suit une architecture organisée par domaine fonctionnel.

```text
Frontend
   │
   │ HTTP / JSON
   ▼
Controllers REST
   │
   ▼
Services métier
   │
   ▼
Repositories
   │
   ▼
JPA / Hibernate
   │
   ▼
MySQL
```

L'authentification suit le flux :

```text
Client
  │
  │ email + password
  ▼
AuthController
  │
  ▼
AuthService
  │
  ▼
AuthenticationManager
  │
  ▼
UserDetailsService
  │
  ▼
Base de données
  │
  ▼
JWT généré
```

Pour les requêtes protégées :

```text
HTTP Request
     │
     │ Authorization: Bearer JWT
     ▼
JwtAuthenticationFilter
     │
     ▼
SecurityContext
     │
     ▼
CurrentUserService
     │
     ▼
Services métier
```

---

# 🛠️ Technologies utilisées

## Backend

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- Hibernate
- Jakarta Validation
- Maven

## Base de données

- MySQL

## Sécurité

- Spring Security
- JSON Web Token
- BCrypt
- contrôle d'accès par rôles

## Frontend

La première interface du projet utilise :

- HTML5
- CSS3
- JavaScript

Le frontend est conçu pour être responsive et adapté aux :

- ordinateurs ;
- tablettes ;
- smartphones.

---

# 📂 Structure du projet

Exemple simplifié :

```text
novaimmo/
│
├── pom.xml
├── README.md
│
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── novaimmo/
        │           └── demo/
        │
        │               ├── auth/
        │               │   ├── JwtService.java
        │               │   ├── JwtAuthenticationFilter.java
        │               │   └── CurrentUserService.java
        │               │
        │               ├── config/
        │               │   └── SecurityConfig.java
        │               │
        │               ├── user/
        │               ├── property/
        │               ├── appointment/
        │               ├── transaction/
        │               ├── payment/
        │               ├── transactiondocument/
        │               ├── project/
        │               ├── partner/
        │               └── exception/
        │
        └── resources/
            └── application.properties
```

Chaque domaine contient généralement :

```text
Entity
Repository
Service
Controller
DTO
```

Par exemple :

```text
project/
│
├── Project.java
├── ProjectRepository.java
├── ProjectService.java
├── ProjectController.java
│
└── dto/
    ├── CreateProjectRequest.java
    └── ProjectResponse.java
```

---

# 🧩 Modèle métier

Les principaux domaines du système sont :

```text
User
 │
 ├── Client
 ├── Agent
 └── Admin

Property
 │
 ├── Images
 ├── Visits
 └── Transactions

Transaction
 │
 ├── Payments
 └── Documents

Project

Partner

Appointment

Contact
```

---

# 👤 Gestion des utilisateurs

Les utilisateurs sont associés à un rôle.

Principaux rôles :

```text
ADMIN
AGENT
CLIENT
PARTNER
```

Un utilisateur contient notamment :

```text
id
nom
prenom
email
password
telephone
role
actif
created_at
updated_at
```

Les mots de passe ne sont pas stockés en clair.

Ils sont encodés avec :

```text
BCrypt
```

---

# 🔐 Authentification et sécurité

NovaImmo utilise une authentification **JWT stateless**.

Après connexion :

```text
POST /api/auth/login
```

le serveur retourne un token JWT.

Le client doit ensuite envoyer :

```http
Authorization: Bearer TOKEN
```

pour accéder aux ressources protégées.

## Inscription

```http
POST /api/auth/register
```

Exemple :

```json
{
  "nom": "Joseph",
  "prenom": "Marc",
  "email": "marc@example.com",
  "password": "123456",
  "telephone": "+509 3700 0000"
}
```

## Connexion

```http
POST /api/auth/login
```

```json
{
  "email": "marc@example.com",
  "password": "123456"
}
```

Réponse indicative :

```json
{
  "token": "eyJhbGciOi...",
  "userId": 1,
  "nom": "Joseph",
  "email": "marc@example.com",
  "role": "CLIENT"
}
```

---

# 🏡 Gestion des propriétés

Le module `Property` constitue le catalogue immobilier.

Il permet notamment :

- création ;
- consultation ;
- modification ;
- suppression ;
- gestion du statut ;
- association d'images.

La consultation des propriétés est publique.

Les opérations administratives sont réservées aux rôles autorisés.

Exemple :

```text
GET     /api/properties
GET     /api/properties/{id}
POST    /api/properties
PUT     /api/properties/{id}
PATCH   /api/properties/{id}
DELETE  /api/properties/{id}
```

---

# 📅 Gestion des visites

Un visiteur peut demander à visiter une propriété.

Le workflow général est :

```text
DEMANDEE
    │
    ▼
CONFIRMEE
    │
    ▼
TERMINEE
```

Une visite peut également être :

```text
REPORTEE
ANNULEE
```

Pour un client authentifié, le backend récupère automatiquement son identité depuis le JWT.

```text
JWT
 │
 ▼
CurrentUserService
 │
 ▼
User ID
 │
 ▼
client_id
```

Le client peut consulter ses propres visites avec :

```http
GET /api/visits/me
```

---

# 📆 Gestion des rendez-vous

Le module `Appointment` permet de gérer des rencontres qui ne sont pas nécessairement directement associées à la visite d'une propriété.

Exemples :

- rendez-vous à l'agence ;
- consultation immobilière ;
- rencontre avec un investisseur ;
- présentation d'un projet ;
- discussion avec un partenaire.

Statuts :

```text
DEMANDE
CONFIRME
REPORTE
ANNULE
TERMINE
```

Un utilisateur connecté peut consulter ses rendez-vous :

```http
GET /api/appointments/me
```

---

# 🤝 Gestion des transactions

Une transaction représente une opération commerciale concernant une propriété.

Types prévus :

```text
VENTE
LOCATION
INVESTISSEMENT
```

Workflow :

```text
EN_NEGOCIATION
      │
      ▼
EN_ATTENTE
      │
      ▼
CONFIRMEE
      │
      ▼
TERMINEE
```

Une transaction peut également devenir :

```text
ANNULEE
```

Lorsqu'une vente est terminée :

```text
Property.status = VENDU
```

Pour une location :

```text
Property.status = LOUE
```

Le client peut consulter ses transactions :

```http
GET /api/transactions/me
```

---

# 💳 Gestion des paiements

Une transaction peut contenir plusieurs paiements.

Exemple :

```text
Transaction : 185 000 USD

Paiement 1 : 50 000 USD
Paiement 2 : 75 000 USD
Paiement 3 : 60 000 USD

Total payé : 185 000 USD
Solde : 0 USD
```

Statuts prévus :

```text
EN_ATTENTE
PAYE
ECHOUE
REMBOURSE
```

Le système peut calculer :

```text
montant transaction
total payé
solde restant
paiement complet
```

Exemple d'endpoint :

```http
GET /api/transactions/{id}/payments/summary
```

---

# 📄 Gestion documentaire

Les documents sont associés aux transactions.

Types actuellement prévus :

```text
CONTRAT
PROMESSE_VENTE
RECU
FACTURE
PIECE_IDENTITE
TITRE_PROPRIETE
AUTRE
```

Une transaction peut donc avoir plusieurs documents :

```text
Transaction
   │
   ├── contrat.pdf
   ├── facture.pdf
   ├── recu.pdf
   └── titre-propriete.pdf
```

Les métadonnées sont conservées dans la base de données.

La première version stocke notamment :

```text
type_document
nom_fichier
fichier_url
```

Une solution de stockage externe pourra être intégrée ultérieurement.

---

# 🏗️ Gestion des projets

NovaImmo permet également de présenter et suivre des projets immobiliers.

Un projet peut contenir :

```text
reference
nom
description
localisation
latitude
longitude
budget
devise
statut
image
```

Statuts :

```text
ETUDE
PLANIFIE
EN_COURS
TERMINE
SUSPENDU
```

Les coordonnées géographiques permettront notamment de positionner les projets sur une carte.

---

# 🤝 Gestion des partenaires

Le module `Partner` permet de gérer différents acteurs liés aux activités immobilières.

Types prévus :

```text
INVESTISSEUR
PROMOTEUR
PROPRIETAIRE
ENTREPRISE
INSTITUTION
```

Un partenaire peut être :

```text
ACTIF
INACTIF
```

Cette partie pourra évoluer vers un véritable espace partenaire.

---

# 🌐 API REST

Quelques routes importantes :

| Méthode | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Inscription |
| POST | `/api/auth/login` | Connexion |
| GET | `/api/properties` | Propriétés |
| GET | `/api/properties/{id}` | Détail propriété |
| POST | `/api/properties` | Créer une propriété |
| POST | `/api/contacts` | Envoyer un contact |
| GET | `/api/visits/me` | Mes visites |
| GET | `/api/appointments/me` | Mes rendez-vous |
| GET | `/api/transactions/me` | Mes transactions |
| POST | `/api/transactions` | Créer une transaction |
| POST | `/api/transactions/{id}/payments` | Ajouter un paiement |
| GET | `/api/transactions/{id}/payments/summary` | Résumé financier |
| POST | `/api/transactions/{id}/documents` | Ajouter un document |
| GET | `/api/transaction-documents/me` | Mes documents |
| GET | `/api/projects` | Projets |
| GET | `/api/projects/active` | Projets actifs |
| GET | `/api/partners` | Partenaires |

---

# 🗄️ Base de données

Le projet utilise actuellement :

```text
MySQL
```

Principales tables :

```text
roles
users

properties
property_images

contacts

property_visits
appointments

transactions
payments
transaction_documents

projects
partners
```

Relations principales :

```text
User
 ├──── PropertyVisit
 ├──── Appointment
 └──── Transaction

Property
 ├──── PropertyImage
 ├──── PropertyVisit
 └──── Transaction

Transaction
 ├──── Payment
 └──── TransactionDocument
```

---

# 💻 Installation

## Prérequis

Installer :

- Java ;
- Maven ;
- MySQL ;
- Git.

Vérifier Java :

```bash
java -version
```

Vérifier Maven :

```bash
mvn -version
```

Vérifier Git :

```bash
git --version
```

---

# 📥 Récupération du projet

```bash
git clone URL_DU_REPOSITORY
```

Puis :

```bash
cd novaimmo
```

---

# ⚙️ Configuration

Configurer :

```text
src/main/resources/application.properties
```

Exemple de configuration locale :

```properties
spring.application.name=novaimmo

spring.datasource.url=jdbc:mysql://localhost:3306/novaimmo
spring.datasource.username=root
spring.datasource.password=VOTRE_MOT_DE_PASSE

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.jpa.properties.hibernate.format_sql=true

app.jwt.secret=REMPLACER_PAR_UN_SECRET_LONG_ET_SECURISE
app.jwt.expiration=86400000
```

> Ne jamais publier les vrais mots de passe de base de données ou secrets JWT dans un repository public.

Pour la production, les secrets devront être placés dans des variables d'environnement.

---

# ▶️ Démarrage

Compiler :

```bash
mvn clean compile
```

Exécuter les tests :

```bash
mvn test
```

Démarrer :

```bash
mvn spring-boot:run
```

Par défaut, l'API sera généralement disponible sur :

```text
http://localhost:8080
```

---

# 🧪 Tests de l'API

## Inscription

```bash
curl -X POST \
http://localhost:8080/api/auth/register \
-H "Content-Type: application/json" \
-d '{
  "nom": "Joseph",
  "prenom": "Marc",
  "email": "marc@example.com",
  "password": "123456",
  "telephone": "+509 3700 0000"
}'
```

## Login

```bash
curl -X POST \
http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{
  "email": "marc@example.com",
  "password": "123456"
}'
```

Après authentification :

```bash
TOKEN="VOTRE_JWT"
```

Exemple :

```bash
curl \
http://localhost:8080/api/appointments/me \
-H "Authorization: Bearer $TOKEN"
```

---

# 🛡️ Sécurité des endpoints

Le système utilise principalement quatre profils :

| Fonction | Public | CLIENT | AGENT | ADMIN |
|---|:---:|:---:|:---:|:---:|
| Consulter propriétés | ✅ | ✅ | ✅ | ✅ |
| Envoyer contact | ✅ | ✅ | ✅ | ✅ |
| Demander visite | ✅ | ✅ | ✅ | ✅ |
| Demander rendez-vous | ✅ | ✅ | ✅ | ✅ |
| Voir ses visites | ❌ | ✅ | ✅ | ✅ |
| Voir ses rendez-vous | ❌ | ✅ | ✅ | ✅ |
| Voir ses transactions | ❌ | ✅ | ✅ | ✅ |
| Gérer propriétés | ❌ | ❌ | ✅ | ✅ |
| Gérer visites | ❌ | Limité | ✅ | ✅ |
| Gérer rendez-vous | ❌ | Limité | ✅ | ✅ |
| Gérer transactions | ❌ | ❌ | ✅ | ✅ |
| Gérer paiements | ❌ | ❌ | ✅ | ✅ |
| Administration | ❌ | ❌ | ❌ | ✅ |

La sécurité repose sur :

```text
Spring Security
      +
JWT
      +
BCrypt
      +
Roles
      +
@PreAuthorize
```

---

# ⚠️ Gestion des erreurs

NovaImmo dispose d'une architecture permettant de retourner des erreurs API structurées.

Exemple :

```json
{
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Transaction introuvable",
  "path": "/api/transactions/99",
  "timestamp": "2026-08-15T08:00:00"
}
```

Exceptions métier prévues :

```text
ResourceNotFoundException
BusinessException
```

avec un gestionnaire global basé sur :

```java
@RestControllerAdvice
```

---

# 📍 Ancrage local : Jacmel, Haïti

NovaImmo met particulièrement en valeur **Jacmel** dans son identité et dans son interface publique.

Le site vitrine prévoit notamment :

- mise en avant de Jacmel ;
- localisation du siège social ;
- carte géographique ;
- présentation des opportunités immobilières locales ;
- propriétés géolocalisées ;
- projets immobiliers géolocalisés.

Les champs :

```text
latitude
longitude
```

préparent également l'intégration future de cartes interactives.

---

# 🔮 Évolution prévue

NovaImmo a été conçu pour évoluer progressivement.

## Court terme

- connecter le frontend à l'API ;
- chargement dynamique des propriétés ;
- chargement dynamique des projets ;
- formulaire de contact connecté ;
- demande de visite connectée ;
- authentification frontend ;
- dashboard client ;
- dashboard agent ;
- dashboard administrateur.

## Gestion immobilière

- favoris ;
- recherche avancée ;
- filtres ;
- comparaison de propriétés ;
- historique des prix ;
- gestion des propriétaires ;
- mandats immobiliers ;
- offres d'achat ;
- négociations.

## Finance

- échéanciers ;
- acomptes ;
- factures ;
- reçus ;
- commissions d'agents ;
- historique financier ;
- rapports financiers.

## Documents

- upload réel des fichiers ;
- stockage cloud ;
- contrats ;
- génération PDF ;
- signatures électroniques ;
- contrôle d'accès aux documents.

## Géolocalisation

- carte interactive ;
- propriétés sur carte ;
- projets sur carte ;
- recherche géographique ;
- mise en valeur de Jacmel et des zones environnantes.

## Notifications

- email ;
- SMS ;
- rappels de rendez-vous ;
- confirmation de visite ;
- notification de paiement ;
- notification de changement de statut.

## Architecture future

L'API pourra alimenter :

```text
                    NovaImmo API
                         │
          ┌──────────────┼──────────────┐
          │              │              │
          ▼              ▼              ▼
      Site Web      Application      Dashboard
                       mobile           Admin
          │              │              │
          └──────────────┼──────────────┘
                         │
                         ▼
                       MySQL
```

---

# 📈 Vision

NovaImmo ne se limite pas à un simple site d'annonces immobilières.

La vision est de construire progressivement un **écosystème numérique immobilier** capable de connecter :

```text
Propriétaires
      │
      ▼
   NovaImmo
      │
 ┌────┼────────────┐
 ▼    ▼            ▼
Clients Agents  Investisseurs
      │
      ▼
  Partenaires
```

L'application pourra ainsi soutenir les activités de vente, location, investissement, développement immobilier et gestion de projets.

---

# 🏢 NovaImmo

**Immobilier • Investissement • Développement**

📍 **Jacmel, Haïti**

---

## 📌 Statut du projet

🚧 **Projet en développement actif**

Backend Spring Boot en cours d'implémentation.

Principaux modules déjà structurés :

- [x] Utilisateurs
- [x] Rôles
- [x] Authentification JWT
- [x] Propriétés
- [x] Images
- [x] Contacts
- [x] Visites
- [x] Rendez-vous
- [x] Transactions
- [x] Paiements
- [x] Documents de transaction
- [x] Projets immobiliers
- [x] Partenaires
- [x] Gestion globale des erreurs
- [ ] Connexion complète frontend/API
- [ ] Dashboard client
- [ ] Dashboard agent
- [ ] Dashboard administrateur
- [ ] Upload réel de documents
- [ ] Déploiement production

---

## 📄 Licence

Le projet est actuellement développé comme une application propriétaire.

Toute utilisation, reproduction ou distribution du code doit être autorisée par le propriétaire du projet.