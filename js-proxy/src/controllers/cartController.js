// js-api/src/controllers/cartController.js
const axios = require('axios');

// Base URL of the Java backend
const JAVA_BACKEND_URL = process.env.JAVA_BACKEND_URL || 'http://localhost:8080';

// Add an item to the cart
const addItem = async (req, res, next) => {
  try {
    const response = await axios.post(`${JAVA_BACKEND_URL}/api/cart/add`, req.body);
    res.status(200).json({ message: response.data });
  } catch (error) {
    next(error); // Pass to error handler
  }
};

// Get all items in the cart
const getAllItems = async (req, res, next) => {
  try {
    const response = await axios.get(`${JAVA_BACKEND_URL}/api/cart/getAllItems`);
    res.status(200).json(response.data);
  } catch (error) {
    next(error);
  }
};

// Get the total cost of the cart
const getTotalCost = async (req, res, next) => {
  try {
    const response = await axios.get(`${JAVA_BACKEND_URL}/api/cart/getTotal`);
    res.status(200).json(response.data);
  } catch (error) {
    next(error);
  }
};

module.exports = {
  addItem,
  getAllItems,
  getTotalCost,
};