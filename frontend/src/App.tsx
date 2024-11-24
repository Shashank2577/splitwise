import React, { useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { AuthProvider } from './contexts/AuthContext';
import theme from './theme';
import useCapacitor from './hooks/useCapacitor';

// Lazy load components
const Login = React.lazy(() => import('./pages/Login'));
const Register = React.lazy(() => import('./pages/Register'));
const Profile = React.lazy(() => import('./pages/Profile'));
const Layout = React.lazy(() => import('./components/Layout'));

const App: React.FC = () => {
  const { initialize, isNative } = useCapacitor();

  useEffect(() => {
    initialize().catch(console.error);
  }, [initialize]);

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider>
        <Router>
          <React.Suspense fallback={<div>Loading...</div>}>
            <Routes>
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/" element={<Layout />}>
                <Route path="profile" element={<Profile />} />
                <Route index element={<Navigate to="/profile" replace />} />
              </Route>
            </Routes>
          </React.Suspense>
        </Router>
      </AuthProvider>
    </ThemeProvider>
  );
};

export default App;
