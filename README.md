# Tablia 🥘 | Smart Meal Planner & Recipe Discovery

Tablia is a robust Android application designed to bridge the gap between global culinary discovery and organized healthy eating. It empowers users to explore thousands of recipes from diverse cultures and seamlessly integrate them into a personalized weekly meal schedule.

Built with a focus on **Reactive Programming** and **Clean Architecture principles (MVP)**, Tablia ensures a fluid user experience even in offline environments.

---

## 📸 App Screenshots

<table style="width: 100%; text-align: center;">
  <tr>
    <td><b>Splash & Onboarding</b></td>
    <td><b>Authentication</b></td>
    <td><b>Home Dashboard</b></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/6eb19a58-428e-4fd5-9e5e-40178d21bf66" width="200" alt="Splash Screen" /></td>
    <td><img src="https://github.com/user-attachments/assets/4d8cab68-cd72-4b8b-b535-7d1548ce8fdb" width="200" alt="Login" /></td>
    <td><img src="https://github.com/user-attachments/assets/762d6693-5baf-4155-a0dc-21999cce4442" width="200" alt="Home" /></td>
  </tr>
  <tr>
    <td><b>Search & Discovery</b></td>
    <td><b>Meal Details</b></td>
    <td><b>Weekly Planner</b></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/6d9b7118-fded-43e4-8e10-65a7b226ea0b" width="200" alt="Search" /></td>
    <td><img src="https://github.com/user-attachments/assets/bd3b21ae-5084-49d7-8c71-0dd84e60bbbb" width="200" alt="Details" /></td>
    <td><img src="https://github.com/user-attachments/assets/795d56bd-92bf-4efa-8b23-c02ac4db060a" width="200" alt="Planner" /></td>
  </tr>
</table>

---

## ✨ Key Features

- **🌍 Global Discovery**: Browse recipes by **Cuisine (Area)**, **Food Category**, or **Main Ingredients** via TheMealDB API.
- **📅 Weekly Planner**: A dedicated scheduling system to plan breakfast, lunch, or dinner for any day of the week.
- **❤️ Local Favorites**: Save recipes to a personalized list accessible anytime, powered by **Room Database**.
- **🔍 Advanced Filtering**: Intelligent search functionality to find exactly what you're looking for by name or origin.
- **📹 Step-by-Step Visuals**: Integrated **YouTube Player** for high-quality video cooking instructions.
- **🔐 Secure Sync**: Cloud-based authentication and data synchronization via **Firebase** to keep your plans safe across devices.
- **📶 Offline Resilience**: Full access to your saved recipes and planned meals without an active internet connection.
- **🎨 Modern UI**: Material Design 3 components, smooth Lottie animations, and a standardized color system.

---

## 🛠 Tech Stack & Architecture

### **Architecture**
- **MVP (Model-View-Presenter)**: Decouples business logic from the UI for better testability and maintenance.
- **Repository Pattern**: Centralized data management between local and remote sources.

### **Libraries & Tools**
- **Networking**: [Retrofit](https://square.github.io/retrofit/) & [RxJava 3](https://github.com/ReactiveX/RxJava) for reactive API handling.
- **Database**: [Room Persistence Library](https://developer.android.com/training/data-storage/room) for robust local caching.
- **Images**: [Glide](https://github.com/bumptech/glide) for optimized image loading and caching.
- **Animations**: [Lottie](https://airbnb.io/lottie/) for premium splash and loading states.
- **Backend**: [Firebase Auth](https://firebase.google.com/products/auth) & [Cloud Firestore](https://firebase.google.com/products/firestore).

---

## 🏗 Project Structure

```text
com.example.tablia
├── data
│   ├── auth          # Firebase authentication logic & models
│   └── meals         # Local (Room) & Remote (Retrofit) data sources
├── presentation      # UI Layer following MVP
│   ├── auth          # Login & SignUp modules
│   ├── home          # Dashboard & Random meal logic
│   ├── search        # Multi-filter discovery logic
│   ├── planner       # Calendar & scheduling management
│   └── meal_details  # Instruction & Video integration
└── utils             # Network observers & UI helpers
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Iguana or newer.
- Firebase project credentials (`google-services.json`).

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/MahmoudRaafat/Tablia.git
   ```
2. Place your `google-services.json` in the `app/` directory.
3. Build the project and run on an emulator or physical device.

---

Developed with ❤️ by [Mahmoud Raafat](https://github.com/MahmoudRaafat)
