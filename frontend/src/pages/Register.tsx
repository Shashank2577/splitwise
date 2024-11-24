import React, { useState } from 'react';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import {
  Container,
  Box,
  Typography,
  Button,
  Link,
  Paper,
  Alert,
} from '@mui/material';
import FormField from '../components/FormField';
import { useAuth } from '../contexts/AuthContext';
import { RegisterRequest } from '../types/auth';

const schema = yup.object({
  name: yup.string().required('Name is required'),
  email: yup.string().email('Invalid email').required('Email is required'),
  password: yup
    .string()
    .required('Password is required')
    .min(6, 'Password must be at least 6 characters'),
  phoneNumber: yup.string().optional(),
});

const Register: React.FC = () => {
  const navigate = useNavigate();
  const { register } = useAuth();
  const [error, setError] = useState<string>('');

  const { control, handleSubmit } = useForm<RegisterRequest>({
    resolver: yupResolver(schema),
    defaultValues: {
      name: '',
      email: '',
      password: '',
      phoneNumber: '',
    },
  });

  const onSubmit = async (data: RegisterRequest) => {
    try {
      await register(data);
      navigate('/');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to register');
    }
  };

  return (
    <Container component="main" maxWidth="xs">
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Paper
          elevation={3}
          sx={{
            p: 4,
            width: '100%',
            borderRadius: 2,
          }}
        >
          <Typography component="h1" variant="h5" align="center" gutterBottom>
            Create Account
          </Typography>

          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}

          <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate>
            <FormField
              name="name"
              control={control}
              label="Full Name"
              autoComplete="name"
              autoFocus
            />
            <FormField
              name="email"
              control={control}
              label="Email Address"
              autoComplete="email"
            />
            <FormField
              name="password"
              control={control}
              label="Password"
              type="password"
              autoComplete="new-password"
            />
            <FormField
              name="phoneNumber"
              control={control}
              label="Phone Number (Optional)"
              autoComplete="tel"
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Sign Up
            </Button>
            <Box sx={{ textAlign: 'center' }}>
              <Link component={RouterLink} to="/login" variant="body2">
                Already have an account? Sign In
              </Link>
            </Box>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
};

export default Register;
