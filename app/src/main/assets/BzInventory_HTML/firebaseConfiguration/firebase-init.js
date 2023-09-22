// Import the functions you need from the SDKs you need
  import { initializeApp } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-app.js";
  import { getAnalytics } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-analytics.js";
  // TODO: Add SDKs for Firebase products that you want to use
  // https://firebase.google.com/docs/web/setup#available-libraries

  // Your web app's Firebase configuration
  // For Firebase JS SDK v7.20.0 and later, measurementId is optional
  const firebaseConfig = {
    apiKey: "AIzaSyBZCu_uudXFI6iHMFgLz4bqaZCyzWewQxA",
    authDomain: "bzinventory-ba9a7.firebaseapp.com",
    projectId: "bzinventory-ba9a7",
    storageBucket: "bzinventory-ba9a7.appspot.com",
    messagingSenderId: "656296737637",
    appId: "1:656296737637:web:cb3c8f6e0a97d655723f8c",
    measurementId: "G-LQ04BY2W69"
  };

  // Initialize Firebase
  /*const app = initializeApp(firebaseConfig);
  const analytics = getAnalytics(app);*/
  firebase.initializeApp(firebaseConfig);
  const db = firebase.firestore();

/*var firebaseConfig = {
      apiKey: "AIzaSyB9J_6jk2_uLkfhCwd3wsP1Qq3musB7aWw",
      authDomain: "fir-db-for-spring-boot-3c025.firebaseapp.com",
      databaseURL: "https://fir-db-for-spring-boot-3c025-default-rtdb.firebaseio.com",
      projectId: "fir-db-for-spring-boot-3c025",
      storageBucket: "fir-db-for-spring-boot-3c025.appspot.com",
      messagingSenderId: "545232367068",
      appId: "1:545232367068:web:99493eca7ddef1a46b3319",
      measurementId: "G-PT33CET673"
};

firebase.initializeApp(firebaseConfig);
const db = firebase.firestore();*/
