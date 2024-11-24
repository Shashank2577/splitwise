import React, { createContext, useContext, useState, useEffect } from 'react';
import { AuthResponse, LoginRequest, RegisterRequest, ProfileResponse } from '../types/auth';
import { authApi, userApi } from '../services/api';

interface AuthContextType {
  user: ProfileResponse | null;
  isAuthenticated: boolean;
  login: (data: LoginRequest) => Promise<void>;
  register: (data: RegisterRequest) => Promise<void>;
  logout: () => void;
  loading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<ProfileResponse | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      loadUser();
    } else {
      setLoading(false);
    }
  }, []);

  const loadUser = async () => {
    try {
      const profile = await userApi.getProfile();
      setUser(profile);
    } catch (error) {
      localStorage.removeItem('token');
    } finally {
      setLoading(false);
    }
  };

  const handleAuthResponse = async (response: AuthResponse) => {
    localStorage.setItem('token', response.token);
    await loadUser();
  };

  const login = async (data: LoginRequest) => {
    const response = await authApi.login(data);
    await handleAuthResponse(response);
  };

  const register = async (data: RegisterRequest) => {
    const response = await authApi.register(data);
    await handleAuthResponse(response);
  };

  const logout = () => {
    localStorage.removeItem('token');
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        login,
        register,
        logout,
        loading,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
