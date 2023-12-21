
/**
 * @swagger
 * /api/auth/register:
 *   post:
 *     summary: User Registration
 *     tags:
 *       - Register
 *     requestBody:
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               username:
 *                 type: string
 *               email:
 *                 type: string
 *               password:
 *                 type: string
 *             required:
 *               - username
 *               - email
 *               - password
 *     responses:
 *       201:
 *         description: User registered successfully
 *         content:
 *           application/json:
 *             example:
 *               status: success
 *               data:
 *                 message: User registered successfully
 *                 token: <user_token>
 */



/**
 * @swagger
 * /api/auth/login:
 *   post:
 *     summary: User Login
 *     tags:
 *       - Authentication
 *     requestBody:
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               email:
 *                 type: string
 *               password:
 *                 type: string
 *     responses:
 *       200:
 *         description: User logged in successfully
 *         content:
 *           application/json:
 *             example:
 *               status: success
 *               data:
 *                 message: User logged in successfully
 *                 token: <user_token>
 */

/**
 * @swagger
 * /api/auth/logout:
 *   post:
 *     summary: User Logout
 *     tags:
 *       - Authentication
 *     responses:
 *       200:
 *         description: User logged out successfully
 *         content:
 *           application/json:
 *             example:
 *               status: success
 *               message: User logged out successfully
 */

/**
 * @swagger
 * /api/auth/reset-password:
 *   post:
 *     summary: Reset Password
 *     tags:
 *       - Authentication
 *     requestBody:
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               email:
 *                 type: string
 *               newPassword:
 *                 type: string
 *     responses:
 *       200:
 *         description: Password reset token generated successfully
 *         content:
 *           application/json:
 *             example:
 *               message: Password reset token generated successfully
 *               token: <reset_token>
 */

/**
 * @swagger
 * /api/personality/{email}:
 *   post:
 *     summary: Set User Personality
 *     tags:
 *       - User Profile
 *     parameters:
 *       - in: path
 *         name: email
 *         required: true
 *         schema:
 *           type: string
 *     requestBody:
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               personality:
 *                 type: string
 *     responses:
 *       200:
 *         description: Personality chosen successfully
 *         content:
 *           application/json:
 *             example:
 *               status: success
 *               data:
 *                 message: Personality chosen successfully
 *                 imageUrl: <personality_image_url>
 */

/**
 * @swagger
 * /api/clothing/color-recommendation/{email}:
 *   post:
 *     summary: Get Clothing Color Recommendation
 *     tags:
 *       - Clothing Recommendation
 *     parameters:
 *       - in: path
 *         name: email
 *         required: true
 *         schema:
 *           type: string
 *     requestBody:
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               selectedClothing:
 *                 type: string
 *     responses:
 *       200:
 *         description: Color recommendation fetched successfully
 *         content:
 *           application/json:
 *             example:
 *               status: success
 *               data:
 *                 message: Color recommendation fetched successfully
 *                 colorRecommendations: ['color1', 'color2', 'color3']
 */

/**
 * @swagger
 * /api/favorit-clothes/{email}:
 *   get:
 *     summary: Get Favorite Clothes
 *     tags:
 *       - Favorite Clothes
 *     parameters:
 *       - in: path
 *         name: email
 *         required: true
 *         schema:
 *           type: string
 *     responses:
 *       200:
 *         description: Favorite clothes data fetched successfully
 *         content:
 *           application/json:
 *             example:
 *               status: success
 *               data: ['ClothingType1', 'ClothingType2']

 *   post:
 *     summary: Add Favorite Clothes
 *     tags:
 *       - Favorite Clothes
 *     parameters:
 *       - in: path
 *         name: email
 *         required: true
 *         schema:
 *           type: string
 *     requestBody:
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               clothingType:
 *                 type: string
 *     responses:
 *       200:
 *         description: Favorite clothing item added successfully
 *         content:
 *           application/json:
 *             example:
 *               status: success
 *               message: Favorite clothing item added successfully
 *               data:
 *                 owner: email
 *                 clothingType: 'ClothingType1'
 */

/**
 * @swagger
 * /api/shoprecommendation:
 *   get:
 *     summary: Get Shop Recommendations
 *     tags:
 *       - Shop Recommendation
 *     responses:
 *       200:
 *         description: Shop recommendations fetched successfully
 *         content:
 *           application/json:
 *             example:
 *               status: success
 *               data: {'ClothingType1': { shopName: 'Shop1', shopUrl: 'http://shop1.com' }}

 * @swagger
 * /api/clothing/types/{gender}:
 *   get:
 *     summary: Get Clothing Types by Gender
 *     tags:
 *       - Clothing Types
 *     parameters:
 *       - in: path
 *         name: gender
 *         required: true
 *         schema:
 *           type: string
 *     responses:
 *       200:
 *         description: Clothing types fetched successfully
 *         content:
 *           application/json:
 *             example:
 *               status: success
 *               data: ['ClothingType1', 'ClothingType2']
 */