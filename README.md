Tablia 🥘 - Your Smart Meal Planner & Recipe Discovery App
Tablia is a comprehensive Android application designed to help users discover delicious recipes from around the world and organize their weekly eating habits with an intuitive meal planner. Built using the MVP (Model-View-Presenter) architecture, the app provides a smooth, responsive experience with robust offline support.
+2

📸 App Screenshots
<table>
<tr>
<td><img src="https://github.com/user-attachments/assets/6eb19a58-428e-4fd5-9e5e-40178d21bf66" width="200" height="400" /></td>
<td><img src="https://github.com/user-attachments/assets/4d8cab68-cd72-4b8b-b535-7d1548ce8fdb" width="200" height="400" /></td>
<td><img src="https://github.com/user-attachments/assets/762d6693-5baf-4155-a0dc-21999cce4442" width="200" height="400" /></td>
</tr>
<tr>
<td><img src="https://github.com/user-attachments/assets/6d9b7118-fded-43e4-8e10-65a7b226ea0b" width="200" height="400" /></td>
<td><img src="https://github.com/user-attachments/assets/bd3b21ae-5084-49d7-8c71-0dd84e60bbbb" width="200" height="400" /></td>
<td><img src="https://github.com/user-attachments/assets/795d56bd-92bf-4efa-8b23-c02ac4db060a" width="200" height="400" /></td>
</tr>
</table>

✨ Features

🌍 Global Recipe Discovery: Explore meals categorized by cuisine (Area), food type (Category), or main ingredient.
+1


🏠 Personal Home Screen: Features a "Meal of the Day" for inspiration and popular recipes.


📅 Weekly Meal Planner: Schedule meals for specific days of the week to stay organized.


❤️ Favorites System: Save recipes to a personal list using Room for local persistence.


🔍 Advanced Search: Search by name or browse through filtered lists of categories and countries.
+1


📹 Video Instructions: Integrated YouTube player for embedded, step-by-step visual cooking guides.


🔐 User Authentication: Secure Login/Sign-Up via Firebase, including social authentication and Guest mode.
+1


📶 Offline Support: View your planned meals and favorites even without an internet connection.


🔄 Data Sync: Synchronize and backup data to the cloud to retrieve it upon login.
+2

🛠 Tech Stack

Architecture: MVP (Model-View-Presenter).


Language: Java.
+1


Networking: Retrofit with RxJava 3 for mandatory reactive API handling.
+1


Local Database: Room Persistence Library for local storage.
+1

UI/UX:

Material Design 3 

Lottie Animations for Splash Screen 

Glide for image loading 


Backend: Firebase Authentication & Firestore.
+1

🏗 Project Structure

data/: Models, local/remote data sources (MealDB API), and repositories.
+1


presentation/: Divided into feature-based packages (Auth, Home, Search, Planner, Favorites, Details), each following the MVP pattern.

utils/: Helpers for network monitoring and image processing.

res/: Centralized design resources, custom fonts (Nunito), and a standardized color system.
