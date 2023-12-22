const express = require('express');
const bodyParser = require('body-parser');
const getConnection = require('./db');
const swaggerJsdoc = require('swagger-jsdoc');
const swaggerUi = require('swagger-ui-express');


const app = express();
const PORT = process.env.PORT || 8081;

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.json());

let users = [];
let userPersonalities = {};
let favoriteClothes = [];

// Swagger Options
const swaggerOptions = {
  definition: {
    openapi: "3.0.0",
    info: {
      title: "ChoSS API Documentation",
      version: "1.0.0",
      description:
        "This is a ChoSS API application made with Express and documented with Swagger",
    },
    servers: [
      {
        url: "https://mysql-dot-choss-api.et.r.appspot.com", // Update the base URL
      },
    ],
  },
  apis: ["swagger.js"], // Update the path to your Swagger definition file
};

const specs = swaggerJsdoc(swaggerOptions);
app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(specs));

// API endpoints

// Register (Create Account)
app.post('/api/auth/register', (req, res) => {
  const { username, email, password } = req.body;
  users.push({ email, password });
  res.json({ status: 'success', data: 'User registered successfully' });
});

app.get('/', (req, res) => {
  const { username, email, password } = req.body;
  users.push({ username, email, password });
  res.json({ status: 'success', data: 'User registered successfully' });
});

// Login
app.post('/api/auth/login', (req, res) => {
  const { email, password } = req.body;
  //const accessToken = 'generated_access_token';
  //res.json({ status: 'success', data: { access_token: accessToken, type: 'bearer' } });
});

// Reset Password
app.post('/api/auth/reset-password', (req, res) => {
  const { email, new_password } = req.body;
  res.json({ status: 'success', data: 'Password reset successful' });
});

// Read User
app.get('/api/users/:email', (req, res) => {
  const { email } = req.params;
  const userData = users.find(user => user.email === email);
  res.json({
      status: 'success',
      data: userData || null
  });
});

app.get('/api/users/:email', async (req, res) => {
  const { email } = req.params;
  try {
      const connection = await getConnection(); // Get a database connection from the pool
      connection.query('SELECT * FROM users WHERE email = ?', [email], (error, results) => {
          connection.release(); // Release the connection back to the pool
          if (error) {
              res.status(500).json({ status: 'error', data: error.message });
          } else {
              res.json({
                  status: 'success',
                  data: results.length > 0 ? results[0] : null
              });
          }
      });
  } catch (err) {
      res.status(500).json({ status: 'error', data: err.message });
  }
});

// Personality
const personalityImages = {
  ISTJ: "https://storage.googleapis.com/choss-407613.appspot.com/MBTI/istj.png",
  ISFJ: "https://storage.googleapis.com/choss-407613.appspot.com/MBTI/isfj.png",
  INFJ: "https://storage.googleapis.com/choss-407613.appspot.com/MBTI/infj.png",
  INTJ: "https://storage.googleapis.com/choss-407613.appspot.com/MBTI/intj.png",
  ISTP: "https://storage.googleapis.com/choss-407613.appspot.com/MBTI/istp.png",
  ISFP: "https://storage.googleapis.com/choss-407613.appspot.com/MBTI/isfp.png",
  INFP: "https://storage.googleapis.com/choss-407613.appspot.com/MBTI/infp.png",
  INTP: "https://storage.googleapis.com/choss-407613.appspot.com/MBTI/intp.png",
  ESTP: "https://storage.googleapis.com/choss-407613.appspot.com/MBTI/estp.png",
  ESFP: "https://storage.googleapis.com/choss-407613.appspot.com/MBTI/esfp.png",
  ENFP: "https://storage.googleapis.com/choss-407613.appspot.com/MBTI/enfp.png",
  ENTP: "https://storage.googleapis.com/choss-407613.appspot.com/MBTI/entp.png",
  ESTJ: "https://storage.googleapis.com/choss-407613.appspot.com/MBTI/estp.png",
  ESFJ: "https://storage.googleapis.com/choss-407613.appspot.com/MBTI/esfj.png",
  ENFJ: "https://storage.googleapis.com/choss-407613.appspot.com/MBTI/enfj.png",
  ENTJ: "https://storage.googleapis.com/choss-407613.appspot.com/MBTI/entj.png",
};


app.post("/api/personality/:email", (req, res) => {
  const { email } = req.params;
  const { personality } = req.body;

  const validPersonalities = [
    "INTJ",
    "INTP",
    "ENTJ",
    "ENTP",
    "INFJ",
    "INFP",
    "ENFJ",
    "ENFP",
    "ISTJ",
    "ISFJ",
    "ESTJ",
    "ESFJ",
    "ISTP",
    "ISFP",
    "ESTP",
    "ESFP",
  ];

  if (!validPersonalities.includes(personality)) {
    return res.json({
      status: "error",
      message: "Invalid personality type specified",
    });
  }

  userPersonalities[email] = personality;

  const imageUrl = personalityImages[personality];

  res.json({
    status: "success",
    data: { message: "Personality chosen successfully", imageUrl },
  });
});


// Add Favotite clothes
app.post('/api/favorit-clothes', async (req, res) => {
  try {
    const { clothesName, email } = req.body;

    if (!clothesName || !email) {
      return res.status(400).json({ status: 'error', message: 'Clothes name and email are required' });
    }

    const insertFavoriteClothesQuery = 'INSERT INTO favoriteClothes (clothesName, email) VALUES (?, ?)';
    await pool.execute(insertFavoriteClothesQuery, [clothesName, email]);

    res.json({ status: 'success', data: 'Favorite clothes added successfully' });
  } catch (error) {
    console.error('Error adding favorite clothes:', error);
    res.status(500).json({ status: 'error', message: 'Internal Server Error' });
  }
});


app.get("/api/favorit-clothes/:email", async (req, res) => {
  const { email } = req.params;

  try {
    const getFavoriteClothesQuery =
      "SELECT * FROM favoriteClothes WHERE email = ?";
    const [favoriteClothesRows] = await pool.execute(getFavoriteClothesQuery, [email]);
    
    if (favoriteClothesRows.length === 0) {
      return res.json({ status: "success", data: [] });
    }
    const userFavoriteClothes = favoriteClothesRows.map((row) => ({
      cloth_Id: row.clothesId,
      clothesName: row.clothesName,
    }));

    res.json({
      status: "success",
      data: userFavoriteClothes,
    });

  } catch (error) {
    console.error("Error fetching favorite clothes:", error);
    res.status(500).json({ status: "error", message: "Internal Server Error" });
  }
});

// Choose Clothing Color Recommendation
app.post("/api/clothing/color-recommendation/:email", (req, res) => {
  const { email } = req.params;
  const { selectedClothing } = req.body;

  // Check if the user has set a personality
  const userPersonality = userPersonalities[email];
  if (!userPersonality) {
    return res.json({
      status: "error",
      message: "User has not set a personality",
    });
  }

  // Color recommendations based on personality
  const warmColors = ["red", "orange", "yellow", "pink", "brown"];
  const coolColors = ["blue", "green", "purple", "teal", "gray"];

  let colorRecommendations;
  if (
    ["INFP", "INFJ", "ENFJ", "ISFP", "ESTJ", "ENTP"].includes(userPersonality)
  ) {
    // Warm personality
    colorRecommendations = warmColors;
  } else {
    // Cool personality
    colorRecommendations = coolColors;
  }

  // You can further customize color recommendations based on the selected clothing type if needed

  res.json({
    status: "success",
    data: {
      message: "Color recommendation fetched successfully",
      colorRecommendations,
    },
  });
});

// clothing types for women or men
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
    "Rok Pendek",
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
    "Celana Pendek",
  ],
};

app.get("/api/clothing/types/:gender", async (req, res) => {
  const { gender } = req.params;

  try {
    const connection = await pool.getConnection();
    const query =
      "SELECT DISTINCT clothingType FROM clothingCategories WHERE gender = ?";
    const [rows] = await connection.execute(query, [gender]);

    if (rows.length === 0) {
      res.json({ status: "success", data: [] });
      return;
    }

    const clothingTypes = rows.map((row) => row.clothingType);
    res.json({ status: "success", data: clothingTypes });

    connection.release();
  } catch (error) {
    console.error("Error fetching clothing types:", error);
    res
      .status(500)
      .json({ status: "error", message: "Failed to fetch clothing types" });
  }
});

// Start the server
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
