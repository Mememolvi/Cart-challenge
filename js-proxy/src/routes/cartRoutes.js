// js-api/src/routes/cartRoutes.js
const express = require('express');
const router = express.Router();
const cartController = require('../controllers/cartController');

// Routes
router.post('/add', cartController.addItem);
router.get('/getAllItems', cartController.getAllItems);
router.get('/getTotal', cartController.getTotalCost);

module.exports = router;