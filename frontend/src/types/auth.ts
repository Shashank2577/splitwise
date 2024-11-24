export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  phoneNumber?: string;
}

export interface AuthResponse {
  token: string;
  email: string;
  name: string;
}

export interface ProfileResponse {
  id: number;
  email: string;
  name: string;
  phoneNumber?: string;
  profileImageUrl?: string;
  fcmToken?: string;
}

export interface UpdateProfileRequest {
  name?: string;
  email?: string;
  phoneNumber?: string;
  fcmToken?: string;
  profileImage?: File;
}
