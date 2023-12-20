require('dotenv').config();
const mysql = require('mysql2/promise');
const express = require('express');
const bodyParser = require('body-parser');
const fs = require('fs');
const jwt = require('jsonwebtoken');
const nodemailer = require('nodemailer'); 
const cors = require('cors'); // mengatasi masalah kebijakan lintas sumber (CORS)


const app = express();
const PORT = process.env.PORT || 3000;

const jwtSecret = process.env.JWT_SECRET;
const emailUsername = process.env.EMAIL_USERNAME;
const emailPassword = process.env.EMAIL_PASSWORD;


app.use(cors()); // Menggunakan cors
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));


let users = [];
let userProfiles = {};
let clothes = [];
let userPersonalities = {};
let favoriteClothes = [];
let passwordResetData = {};
let userProfilePictures = {};




// API endpoints

// Register (Create Account)
app.post('/api/auth/register', (req, res) => {
    const { username, email, password } = req.body;
    users.push({ username, email, password });
    userPersonalities[email] = null;
    res.json({ status: 'success', data: 'User registered successfully' });
});

function authenticateUser(email, password) {
    // Temukan pengguna berdasarkan email
    const user = users.find(user => user.email === email);

    // Periksa apakah pengguna ditemukan dan password sesuai
    if (user && user.password === password) {
        return user;
    } else {
        return null; // Jika email atau password tidak sesuai
    }
}

// Login
app.post('/api/auth/login', (req, res) => {
    const { email, password } = req.body;

    // Melakukan autentikasi, jika berhasil
    const user = authenticateUser(email, password);

    if (user) {
        // Membuat token dengan menggunakan user.username
        const token = jwt.sign({ username: user.username, email }, jwtSecret, { expiresIn: '1h' });

        // Mengirim token sebagai respons
        res.json({ status: 'success', data: { access_token: token, type: 'bearer' } });
    } else {
        res.json({ status: 'error', message: 'Invalid email or password' });
    }
});


// Middleware untuk memeriksa token
function verifyToken(req, res, next) {
    const token = req.headers.authorization;

    if (!token) {
        return res.status(403).json({ status: 'error', message: 'Token not provided' });
    }

    // Memeriksa dan memverifikasi token
    jwt.verify(token, process.env.JWT_SECRET, (err, decoded) => {
        if (err) {
            return res.status(401).json({ status: 'error', message: 'Failed to authenticate token' });
        }

        // Menyimpan informasi pengguna dari token di objek req
        req.user = decoded;
        next();
    });
}

// Contoh penggunaan middleware di endpoint yang dilindungi
app.get('/api/protected-resource', verifyToken, (req, res) => {
    res.json({ status: 'success', data: 'This is a protected resource' });
});

// Logout
app.post('/api/auth/logout', (req, res) => {
    // Perform logout logic if needed
    res.json({ status: 'success', data: 'Logout successful' });
});


async function generateToken(username, email) {
    const secret = jwtSecret;
    return jwt.sign({ username, email }, jwtSecret, { expiresIn: '1h' });
}


// Endpoint untuk melakukan reset password (using JWT)
app.post('/api/auth/reset-password', (req, res) => {
    const { email, newPassword } = req.body;

    // Generate a JWT token
    const token = jwt.sign({ email }, jwtSecret, { expiresIn: '1h' });

    // Save the token or include it in the response
    passwordResetData[email] = { token, createdAt: new Date() };

    // Send the token to the client (you can customize the response as needed)
    res.json({ message: 'Password reset token generated successfully', token });
});


// Placeholder for the uploadProfilePicture function
async function updateUserProfilePicture(email, imageUrl) {
    // Implement your logic to update the user's profile picture in the database
    // Use the provided email and imageUrl parameters in your SQL query or update logic
    // Example: You might want to update the 'profile_picture_url' field in the 'users' table

    const pool = mysql.createPool({
        host: process.env.DB_HOST,
        user: process.env.DB_USER,
        password: process.env.DB_PASSWORD,
        database: process.env.DB_DATABASE,
        waitForConnections: true,
        connectionLimit: 10,
        queueLimit: 0,
        Promise: global.Promise // Enable Promise wrapper
    });
    

    try {
        const updateQuery = 'UPDATE users SET profile_picture_url = ? WHERE email = ?';
        const [rows] = await connection.execute(updateQuery, [imageUrl, email]);
        return rows;
    } catch (error) {
        throw error;
    } finally {
        connection.end();
    }
}

// my profile
app.post('/api/myprofile/:email', (req, res) => {
    const { email } = req.params;
    const { username, profilePicture } = req.body;

    // Membuat atau memperbarui profil pengguna
    userProfiles[email] = { username, email, profilePicture };

    res.json({ status: 'success', data: 'User profile updated successfully' });
});

app.get('/api/myprofile/:email', (req, res) => {
    const { email } = req.params;

    // Periksa apakah pengguna memiliki profil
    if (!userProfiles[email]) {
        return res.json({ status: 'error', message: 'User profile not found' });
    }

    // Mengambil data profil pengguna
    const userProfile = userProfiles[email];
    res.json({ status: 'success', data: userProfile });
});


// Dashboard
app.get('/api/dashboard/:email', (req, res) => {
    const { email } = req.params;
    const dashboardData = {
        status: 'success',
        data: {
            user_data: users.find(user => user.email === email),
            clothing_data: clothes,
            user_personality: userPersonalities[email]
        }
    };
    res.json(dashboardData);
});

// Arrays of warm and cool colors
const warmColors = ['red', 'orange', 'yellow', 'pink', 'brown'];
const coolColors = ['blue', 'green', 'purple', 'teal', 'gray'];

// ...

const personalityImages = {
  'ISTJ': 'https://storage.googleapis.com/choss-407613.appspot.com/MBTI/istj.png',
  'ISFJ': 'https://storage.googleapis.com/choss-407613.appspot.com/MBTI/isfj.png',
  'INFJ': 'https://storage.googleapis.com/choss-407613.appspot.com/MBTI/infj.png',
  'INTJ': 'https://storage.googleapis.com/choss-407613.appspot.com/MBTI/intj.png',
  'ISTP': 'https://storage.googleapis.com/choss-407613.appspot.com/MBTI/istp.png',
  'ISFP': 'https://storage.googleapis.com/choss-407613.appspot.com/MBTI/isfp.png',
  'INFP': 'https://storage.googleapis.com/choss-407613.appspot.com/MBTI/infp.png',
  'INTP': 'https://storage.googleapis.com/choss-407613.appspot.com/MBTI/intp.png',
  'ESTP': 'https://storage.googleapis.com/choss-407613.appspot.com/MBTI/estp.png',
  'ESFP': 'https://storage.googleapis.com/choss-407613.appspot.com/MBTI/esfp.png',
  'ENFP': 'https://storage.googleapis.com/choss-407613.appspot.com/MBTI/enfp.png',
  'ENTP': 'https://storage.googleapis.com/choss-407613.appspot.com/MBTI/entp.png',
  'ESTJ': 'https://storage.googleapis.com/choss-407613.appspot.com/MBTI/estp.png',
  'ESFJ': 'https://storage.googleapis.com/choss-407613.appspot.com/MBTI/esfj.png',
  'ENFJ': 'https://storage.googleapis.com/choss-407613.appspot.com/MBTI/enfj.png',
  'ENTJ': 'https://storage.googleapis.com/choss-407613.appspot.com/MBTI/entj.png',
};

app.post('/api/personality/:email', (req, res) => {
    const { email } = req.params;
    const { personality } = req.body;

    const validPersonalities = ['INTJ', 'INTP', 'ENTJ', 'ENTP', 'INFJ', 'INFP', 'ENFJ', 'ENFP', 'ISTJ', 'ISFJ', 'ESTJ', 'ESFJ', 'ISTP', 'ISFP', 'ESTP', 'ESFP'];

    if (!validPersonalities.includes(personality)) {
        return res.json({ status: 'error', message: 'Invalid personality type specified' });
    }

    userPersonalities[email] = personality;

    const imageUrl = personalityImages[personality];

    res.json({ status: 'success', data: { message: 'Personality chosen successfully', imageUrl } });
});


  // Choose Clothing Color Recommendation
app.post('/api/clothing/color-recommendation/:email', (req, res) => {
    const { email } = req.params;
    const { selectedClothing } = req.body;

    // Check if the user has set a personality
    const userPersonality = userPersonalities[email];
    if (!userPersonality) {
        return res.json({ status: 'error', message: 'User has not set a personality' });
    }

    // Color recommendations based on personality
    let colorRecommendations;
    if (['INFP', 'INFJ', 'ENFJ', 'ISFP', 'ESTJ', 'ENTP'].includes(userPersonality)) {
        // Warm personality
        colorRecommendations = warmColors;
    } else {
        // Cool personality
        colorRecommendations = coolColors;
    }

    // You can further customize color recommendations based on the selected clothing type if needed

    res.json({ status: 'success', data: { message: 'Color recommendation fetched successfully', colorRecommendations } });
});

// Middleware untuk membaca body request yang berformat JSON
app.use(express.json());

// Endpoint to get clothing types for women or men
app.get('/api/clothing/types/:gender', (req, res) => {
    const { gender } = req.params;

    // Check if the gender is valid (women or men)
    if (['women', 'men'].includes(gender)) {
        const clothingTypes = clothesJSON[gender];
        res.json({ status: 'success', data: clothingTypes });
    } else {
        res.json({ status: 'error', message: 'Invalid gender specified' });
    }
});

// Endpoint untuk menampilkan dan mengambil data favorit clothes berdasarkan email
// Endpoint untuk menampilkan dan mengambil data favorit clothes berdasarkan email
app.get('/api/favorit-clothes/:email', (req, res) => {
    const { email } = req.params;
    
    const userFavoriteClothes = clothes.filter(cloth => cloth.owner === email);

    
    res.json({
        status: 'success',
        data: userFavoriteClothes
    });
});


// Endpoint untuk menambahkan pakaian ke favorit berdasarkan kategori
// GET request to retrieve favorite clothes for a user
app.get('/api/favorit-clothes/:email', (req, res) => {
    const { email } = req.params;
    
    const userFavoriteClothes = clothes.filter(cloth => cloth.owner === email);

    res.json({
        status: 'success',
        data: userFavoriteClothes
    });
});

// POST request to add a new favorite clothing item for a user
app.post('/api/favorit-clothes/:email', (req, res) => {
    const { email, clothingType } = req.body;

    // Add the new favorite clothing item to the clothes array
    clothes.push({
        owner: email,
        clothingType: clothingType
    });

    res.json({
        status: 'success',
        message: 'Favorite clothing item added successfully',
        data: {
            owner: email,
            clothingType: clothingType
        }
    });
});

const clothingCategories = {
    women: [
        "Blouse",
        "Dress",
        "Blazer",
        "Cardigan",
        "Hoodie",
        "Jacket",
        "Jumpsuit",
        "Croptop",
        "Kaos Oversize",
        "Kaos Polo",
        "Kaos T-Shirt",
        "Kemeja",
        "Kemeja Denim",
        "Pakaian Olahraga",
        "Sweatshirt",
        "Tunik",
        "Celana Panjang",
        "Celana Pendek",
        "Rok Panjang",
        "Rok Pendek"
    ],
    men: [
        "Blazer",
        "Cardigan",
        "Hoodie",
        "Jacket",
        "Jumpsuit",
        "Croptop",
        "Kaos Oversize",
        "Kaos Polo",
        "Kaos T-Shirt",
        "Kemeja",
        "Kemeja Denim",
        "Pakaian Olahraga",
        "Sweatshirt",
        "Celana Panjang",
        "Celana Pendek"
    ]
};


// Additional endpoint to get clothing categories
app.get('/api/clothing-categories/:gender', (req, res) => {
    const { gender } = req.params;
    const categories = clothingCategories[gender] || [];

    res.json({
        status: 'success',
        data: categories
    });
});

// Endpoint to get clothing types for women or men
app.get('/api/clothing/types/:gender', (req, res) => {
    const { gender } = req.params;

    // Check if the gender is valid (women or men)
    if (['women', 'men'].includes(gender)) {
        const clothingTypes = clothingCategories[gender]; // Use clothingCategories instead of clothesJSON
        res.json({ status: 'success', data: clothingTypes });
    } else {
        res.json({ status: 'error', message: 'Invalid gender specified' });
    }
});

// Define the pool globally
const pool = mysql.createPool({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_DATABASE,
    waitForConnections: true,
    connectionLimit: 10,
    queueLimit: 0
});

// Fetch shop recommendations from the database
// Fetch shop recommendations from the database
// Update fetchShopRecommendations to use async/await
async function fetchShopRecommendations() {
    try {
        const [rows, fields] = await pool.query('SELECT * FROM shopRecommendations');

        if (!rows) {
            console.error('No rows returned from the database');
            return {};
        }

        const shopRecommendations = {};
        rows.forEach(row => {
            if (row.clothingType) {
                shopRecommendations[row.clothingType.toLowerCase()] = {
                    shopName: row.shopName,
                    shopUrl: row.shopUrl,
                };
            }
        });

        return shopRecommendations;
    } catch (error) {
        console.error('Error fetching shop recommendations from the database:', error);
        throw error;
    }
}


// Inside your app.listen callback or wherever appropriate
fetchShopRecommendations()
    .then(shopRecommendationsFromDB => {
        console.log('Shop Recommendations from Database:', shopRecommendationsFromDB);
        // Now, you can use these recommendations as needed in your application
    })
    .catch(error => {
        console.error('Failed to fetch shop recommendations:', error);
    });

// Shop Recommendation
// Endpoint to get shop recommendations
app.get('/api/shoprecommendation', async (req, res) => {
    try {
        // Fetch shop recommendations from the database
        const shopRecommendationsFromDB = await fetchShopRecommendations();

        // Respond with the fetched shop recommendations
        res.json({ status: 'success', data: shopRecommendationsFromDB });
    } catch (error) {
        console.error('Failed to fetch shop recommendations:', error);
        res.status(500).json({ status: 'error', message: 'Failed to fetch shop recommendations' });
    }
});

// Endpoint untuk memberikan rekomendasi toko berdasarkan jenis pakaian yang dipilih
app.post('/api/clothing/shop-recommendation/:email', async (req, res) => {
    const { email } = req.params;
    const { selectedClothing } = req.body;

    // Check if the user has set a personality
    const userPersonality = userPersonalities[email];
    if (!userPersonality) {
        return res.json({ status: 'error', message: 'User has not set a personality' });
    }

    try {
        // Ambil data rekomendasi toko dari database
        const shopRecommendations = await fetchShopRecommendations();

        // Cek apakah ada rekomendasi toko untuk jenis pakaian yang dipilih
        if (shopRecommendations[selectedClothing.toLowerCase()]) {
            const shopRecommendation = shopRecommendations[selectedClothing.toLowerCase()];
            res.json({ status: 'success', data: { message: 'Shop recommendation fetched successfully', shopRecommendation } });
        } else {
            // Handle jika tidak ada rekomendasi toko untuk jenis pakaian yang dipilih
            res.json({ status: 'error', message: 'Shop recommendation not found for the selected clothing type' });
        }
    } catch (error) {
        console.error('Failed to fetch shop recommendations:', error);
        res.status(500).json({ status: 'error', message: 'Failed to fetch shop recommendations' });
    }
});


// Start the server
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});

module.exports = generateToken;
