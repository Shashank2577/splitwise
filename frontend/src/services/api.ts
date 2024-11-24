import axios, { AxiosRequestConfig } from 'axios';
import { 
  LoginRequest, 
  RegisterRequest, 
  AuthResponse, 
  ProfileResponse, 
  UpdateProfileRequest 
} from '../types/auth';

const API_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests if available
api.interceptors.request.use((config: AxiosRequestConfig) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers = config.headers || {};
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authApi = {
  login: async (data: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/auth/login', data);
    return response.data;
  },

  register: async (data: RegisterRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/auth/register', data);
    return response.data;
  },
};

export const userApi = {
  getProfile: async (): Promise<ProfileResponse> => {
    const response = await api.get<ProfileResponse>('/users/profile');
    return response.data;
  },

  updateProfile: async (data: UpdateProfileRequest): Promise<ProfileResponse> => {
    const formData = new FormData();
    Object.entries(data).forEach(([key, value]) => {
      if (value !== undefined) {
        formData.append(key, value);
      }
    });

    const response = await api.put<ProfileResponse>('/users/profile', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },
};
