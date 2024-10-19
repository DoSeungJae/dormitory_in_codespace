import axios from "axios";

const API_BASE_URL = 'https://improved-space-tribble-vjvwrwx956jh69w4-8080.app.github.dev';

const api = axios.create({
    baseURL: API_BASE_URL,
});

export default api;