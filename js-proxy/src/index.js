// js-api/src/index.js
require('dotenv').config();
const express = require('express');
const cartRoutes = require('./routes/cartRoutes');
const errorHandler = require('./utils/errorHandler');

const app = express();

// Middleware to parse JSON bodies
app.use(express.json());

// Mount cart routes
app.use('/api/cart', cartRoutes);

// Global error handler
app.use(errorHandler);

// Start the server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Proxy server running on port ${PORT}`);
});