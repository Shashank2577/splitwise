import React, { useState } from 'react';
import { Box, Button, IconButton, CircularProgress } from '@mui/material';
import { PhotoCamera } from '@mui/icons-material';
import useCapacitor from '../hooks/useCapacitor';

interface ExpenseImageUploadProps {
  onImageCapture: (base64Image: string) => void;
  disabled?: boolean;
}

const ExpenseImageUpload: React.FC<ExpenseImageUploadProps> = ({ onImageCapture, disabled }) => {
  const { isNative, takePicture, vibrate } = useCapacitor();
  const [loading, setLoading] = useState(false);

  const handleCapture = async () => {
    try {
      setLoading(true);
      await vibrate();
      
      const image = await takePicture();
      if (image?.base64String) {
        onImageCapture(`data:image/jpeg;base64,${image.base64String}`);
      }
    } catch (error) {
      console.error('Error capturing image:', error);
    } finally {
      setLoading(false);
    }
  };

  if (isNative) {
    return (
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
        <IconButton
          color="primary"
          aria-label="take picture"
          onClick={handleCapture}
          disabled={disabled || loading}
        >
          {loading ? <CircularProgress size={24} /> : <PhotoCamera />}
        </IconButton>
      </Box>
    );
  }

  // Fallback for web
  return (
    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
      <Button
        variant="contained"
        component="label"
        startIcon={loading ? <CircularProgress size={24} /> : <PhotoCamera />}
        disabled={disabled || loading}
      >
        Upload
        <input
          type="file"
          hidden
          accept="image/*"
          onChange={(e) => {
            const file = e.target.files?.[0];
            if (file) {
              const reader = new FileReader();
              reader.onloadend = () => {
                if (typeof reader.result === 'string') {
                  onImageCapture(reader.result);
                }
              };
              reader.readAsDataURL(file);
            }
          }}
        />
      </Button>
    </Box>
  );
};

export default ExpenseImageUpload;
