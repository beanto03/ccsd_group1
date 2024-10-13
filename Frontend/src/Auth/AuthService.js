//Authservice.js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const AuthService = {
  async login(email, password) {
    try {
      const response = await axios.post(`${API_BASE_URL}/login`, { // Corrected with backticks

        email,
        password,
      }, {
        headers: {
          'Content-Type': 'application/json',
          "Accept": "application/json"
        },
        withCredentials: true
      });

      if (response.status === 200) {
        localStorage.setItem('username', response.data.username);
        localStorage.setItem('role', response.data.role);
        return true;
      }
    } catch (error) {
      console.error('Login failed:', error);
      return false;
    }
  },

  async register({ name, email, password, role }) {
    try {
      const roleNumber = role === "seller" ? 1 : 0;

      const response = await axios.post(`${API_BASE_URL}/register`, {
        name,
        email,
        password,
        role: roleNumber
      }, {
        headers: {
          'Content-Type': 'application/json',

        },    

      });

      if (response.status === 200) {
        console.log('Registration successful:', response.data);
        return true;
      }
    } catch (error) {
      if (error.response) {
        console.error('Registration failed:', error.response.data);
      } else {
        console.error('Registration error:', error.message);
      }
      return false;
    }
  },

  async addProduct({ name, description, startingBid, sellerId, imageBase64Strings }) {
    try {
      const formData = new FormData();
      formData.append('product', JSON.stringify({ name, description, startingBid, sellerId }));

      if (imageBase64Strings) {
        formData.append('images', imageBase64Strings);
      }
  

      // Log the formData for debugging purposes
      console.log('FormData being sent:', formData);

      const response = await axios.post(`${API_BASE_URL}/products/add`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        withCredentials: true
      });

        //take register from user input in frontend 

      // Check for successful response
      if (response.status === 200 || response.status === 201 ) {

        console.log('Product added successfully:', response.data);
        return response.data; // Return the added product
      } else {
        console.error('Failed to add product. Response status:', response.status);
        return null; // Indicate failure
      }
    } catch (error) {
      if (error.response) {
        console.error('Error response from server:', error.response.data);
      } else {
        console.error('Error adding product:', error.message);
      }
      return null; // Indicate failure
    }
  },

  //to fetch bid history

  async getBidHistory(buyerId, productId) {
    try {
      const response = await axios.get(`${API_BASE_URL}/bidHistory/getBids/${buyerId}/${productId}`);
      return response.data;  // Return the fetched bid history
    } catch (error) {
      console.error('Error fetching bid history:', error);
      throw new Error(error.message);
    }
  },

  
};

export default AuthService;