// js-api/src/utils/errorHandler.js
const errorHandler = (error, req, res, next) => {
    console.error(error);
  
    // Handle axios errors
    if (error.response) {
      // Java backend returned an error response (e.g., 400, 500)
      const { status, data } = error.response;
      return res.status(status).json({
        error: data || 'Error from Java backend',
      });
    }
  
    // Handle other errors (e.g., network issues)
    res.status(500).json({
      error: 'Internal Server Error',
      message: error.message,
    });
  };
  
  module.exports = errorHandler;