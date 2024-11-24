import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import {
  Container,
  Box,
  Typography,
  Button,
  Paper,
  Alert,
  Avatar,
  IconButton,
} from '@mui/material';
import { PhotoCamera } from '@mui/icons-material';
import FormField from '../components/FormField';
import { useAuth } from '../contexts/AuthContext';
import { userApi } from '../services/api';
import { UpdateProfileRequest } from '../types/auth';

const schema = yup.object({
  name: yup.string().required('Name is required'),
  email: yup.string().email('Invalid email').required('Email is required'),
  phoneNumber: yup.string().optional(),
});

const Profile: React.FC = () => {
  const { user, loadUser } = useAuth();
  const [error, setError] = useState<string>('');
  const [success, setSuccess] = useState<string>('');
  const [selectedImage, setSelectedImage] = useState<File | null>(null);

  const { control, handleSubmit } = useForm<UpdateProfileRequest>({
    resolver: yupResolver(schema),
    defaultValues: {
      name: user?.name || '',
      email: user?.email || '',
      phoneNumber: user?.phoneNumber || '',
    },
  });

  const handleImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files[0]) {
      setSelectedImage(event.target.files[0]);
    }
  };

  const onSubmit = async (data: UpdateProfileRequest) => {
    try {
      if (selectedImage) {
        data.profileImage = selectedImage;
      }
      await userApi.updateProfile(data);
      await loadUser();
      setSuccess('Profile updated successfully');
      setError('');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to update profile');
      setSuccess('');
    }
  };

  return (
    <Container component="main" maxWidth="sm">
      <Paper elevation={3} sx={{ p: 4, borderRadius: 2 }}>
        <Typography component="h1" variant="h5" align="center" gutterBottom>
          Profile Settings
        </Typography>

        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        {success && (
          <Alert severity="success" sx={{ mb: 2 }}>
            {success}
          </Alert>
        )}

        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            mb: 3,
          }}
        >
          <Box sx={{ position: 'relative' }}>
            <Avatar
              src={selectedImage ? URL.createObjectURL(selectedImage) : user?.profileImageUrl}
              sx={{ width: 100, height: 100, mb: 2 }}
            />
            <IconButton
              color="primary"
              aria-label="upload picture"
              component="label"
              sx={{
                position: 'absolute',
                right: -10,
                bottom: 10,
                backgroundColor: 'background.paper',
                '&:hover': { backgroundColor: 'background.paper' },
              }}
            >
              <input
                hidden
                accept="image/*"
                type="file"
                onChange={handleImageChange}
              />
              <PhotoCamera />
            </IconButton>
          </Box>
        </Box>

        <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate>
          <FormField
            name="name"
            control={control}
            label="Full Name"
            autoComplete="name"
          />
          <FormField
            name="email"
            control={control}
            label="Email Address"
            autoComplete="email"
          />
          <FormField
            name="phoneNumber"
            control={control}
            label="Phone Number"
            autoComplete="tel"
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{ mt: 3 }}
          >
            Save Changes
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default Profile;
