const mysql = require('mysql');

// Configure your Cloud SQL connection details
const config = {
  user: 'root',
  password: '12345',
  database: 'chossv1',
  socketPath: 'choss-api:asia-southeast2:chossv1'
};

// Create a pool to manage connections
const pool = mysql.createPool(config);

// Middleware function to handle database connections
function getConnection() {
  return new Promise((resolve, reject) => {
    pool.getConnection((err, connection) => {
      if (err) {
        reject(err);
      } else {
        resolve(connection);
      }
    });
  });
}

module.exports = getConnection;